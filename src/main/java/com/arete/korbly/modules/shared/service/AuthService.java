package com.arete.korbly.modules.shared.service;

import com.amazonaws.services.s3.AmazonS3;
import com.arete.korbly.infrastructure.integrations.OTPService;
import com.arete.korbly.infrastructure.integrations.S3FileUploadService;
import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.investor.domain.Investor;
import com.arete.korbly.modules.investor.dto.InvestorApplicationDTO;
import com.arete.korbly.modules.investor.dto.InvestorDTO;
import com.arete.korbly.modules.investor.mapper.InvestorMapper;
import com.arete.korbly.modules.investor.persistence.InvestorRepository;
import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.dto.EmailRequest;
import com.arete.korbly.modules.shared.dto.VerificationRequest;
import com.arete.korbly.modules.shared.dto.VerificationResponse;
import com.arete.korbly.modules.shared.dto.VerifyUser;
import com.arete.korbly.modules.shared.enums.UploadFileResponse;
import com.arete.korbly.modules.shared.enums.UserType;
import com.arete.korbly.modules.shared.exceptions.InvalidOTP;
import com.arete.korbly.modules.shared.exceptions.UserNotFound;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.dto.SMEApplicationDTO;
import com.arete.korbly.modules.sme.dto.SMEDTO;
import com.arete.korbly.modules.sme.mapper.SMEMapper;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Optional;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final JWTService jwtService;
    private final InvestorMapper investorMapper;
    private final InvestorRepository investorRepository;
    private final AmazonS3 amazonS3;
    private final S3FileUploadService fileUploadService;
    private final SMERepository smeRepository;
    private final SMEMapper smeMapper;
    private final OTPService otpService;
    private final EmailService emailService;

    public AuthService(JWTService jwtService,
                       InvestorMapper investorMapper,
                       InvestorRepository investorRepository,
                       AmazonS3 amazonS3,
                       S3FileUploadService fileUploadService,
                       SMERepository smeRepository,
                       SMEMapper smeMapper,
                       OTPService otpService,
                       AppUserRepository appUserRepository1, EmailService emailService) {
        this.jwtService = jwtService;
        this.investorMapper = investorMapper;
        this.investorRepository = investorRepository;
        this.amazonS3 = amazonS3;
        this.fileUploadService = fileUploadService;
        this.smeRepository = smeRepository;
        this.smeMapper = smeMapper;
        this.otpService = otpService;
        this.appUserRepository = appUserRepository1;
        this.emailService = emailService;
    }

    @Transactional
    public InvestorDTO onboardInvestor(InvestorApplicationDTO investorApplicationDTO,
                                       MultipartFile certOfIncorporation,
                                       MultipartFile latestAuditedFinancialStatements,
                                       MultipartFile investmentPolicyStatement,
                                       MultipartFile boardResolution) throws IOException {
        AppUser investor = new AppUser();
        investor.setPrimaryContactEmail(investorApplicationDTO.primaryContactEmail());
        investor.setUserType(UserType.INVESTOR);

        Investor newInvestor = Investor.builder()
                .institutionType(investorApplicationDTO.institutionType())
                .investmentFocus(investorApplicationDTO.investmentFocusSet())
                .riskAppetite(investorApplicationDTO.riskAppetite())
                .phoneNumber(investorApplicationDTO.phoneNumber())
                .registrationNumber(investorApplicationDTO.registrationNumber())
                .dateEstablished(investorApplicationDTO.yearEstablished())
                .institutionalAddress(investorApplicationDTO.institutionalAddress())
                .institutionName(investorApplicationDTO.institutionName())
                .assetsUnderManagement(investorApplicationDTO.assetsUnderManagement())
                .minimumInvestment(investorApplicationDTO.minimumInvestment())
                .appUser(investor)
                .build();

        String baseKey = "investors/" + investorApplicationDTO.institutionName().replaceAll("\\s+", "_") + "/";

        //uploading investor files
        //1. cert of incorporation
        UploadFileResponse incCert = uploadInvestorFiles(baseKey + "incorporation.pdf", certOfIncorporation);
        //2. latestAuditedFinancialStatements
        UploadFileResponse invFile = uploadInvestorFiles(baseKey + "lastAuditedFinancialStatement.pdf",latestAuditedFinancialStatements);
        //3. investmentPolicyStatement
        UploadFileResponse policyStmt = uploadInvestorFiles(baseKey + "investmentPolicyStatement.pdf", investmentPolicyStatement);
        //4. boardResolution
        UploadFileResponse boardRes = uploadInvestorFiles(baseKey + "boardResolution.pdf", boardResolution);

        newInvestor.setCertificateOfIncorporationURL(incCert.key());
        newInvestor.setAuditedFinancialStatementsURL(invFile.key());
        newInvestor.setInvestmentPolicyStatementURL(policyStmt.key());
        newInvestor.setBoardResolutionURL(boardRes.key());

        appUserRepository.save(investor);
        investorRepository.save(newInvestor);
        return investorMapper.investorEntityToInvestorDTO(newInvestor);

    }

    public SMEDTO onboardSME(
            SMEApplicationDTO smeApplicationDTO,
            MultipartFile certOfIncorporation,
            MultipartFile latestFinancialStmt,
            MultipartFile businessPlan,
            MultipartFile taxClearanceCert
    ) throws IOException {
        AppUser sme = AppUser.builder()
                .primaryContactEmail(smeApplicationDTO.primaryContactEmail())
                .userType(UserType.BUSINESS)
                .build();
        SME newSME = SME.builder()
                .companyName(smeApplicationDTO.companyName())
                .industry(smeApplicationDTO.industry())
                .registrationNumber(smeApplicationDTO.registrationNumber())
                .phoneNumber(smeApplicationDTO.phoneNumber())
                .region(smeApplicationDTO.region())
                .dateEstablished(smeApplicationDTO.dateEstablished())
                .websiteURL(smeApplicationDTO.websiteURL())
                .businessDescription(smeApplicationDTO.businessDescription())
                .annualRevenue(smeApplicationDTO.annualRevenue())
                .numberOfEmployees(smeApplicationDTO.numberOfEmployees())
                .monthlyRevenue(smeApplicationDTO.monthlyRevenue())
                .requestedAmount(smeApplicationDTO.requestedAmount())
                .purposeOfFunding(smeApplicationDTO.purposeOfFunding())
                .build();

        String baseKey = "smes/" + smeApplicationDTO.companyName().replaceAll("\\s+", "_") + "/";

        //uploading investor files
        //1. cert of incorporation
        UploadFileResponse incCert = uploadInvestorFiles(baseKey + "incorporation.pdf", certOfIncorporation);
        //2. latestAuditedFinancialStatements
        UploadFileResponse financialStmt = uploadInvestorFiles(baseKey + "latestFinancialStmt.pdf", latestFinancialStmt);
        //3. investmentPolicyStatement
        UploadFileResponse businessPlanFile = uploadInvestorFiles(baseKey + "businessPlan.pdf", businessPlan);
        //4. boardResolution
        UploadFileResponse taxClearanceCertFile = uploadInvestorFiles(baseKey + "taxClearanceCert.pdf", taxClearanceCert);

        newSME.setCertOfIncorporation(incCert.key());
        newSME.setLatestFinancialStatements(financialStmt.key());
        newSME.setBusinessPlan(businessPlanFile.key());
        newSME.setTaxClearanceCert(taxClearanceCertFile.key());

        appUserRepository.save(sme);
        smeRepository.save(newSME);
        return smeMapper.smeEntityToSMEDto(newSME);
    }

    public VerificationResponse verifyUserLogin(VerificationRequest request, HttpServletResponse response){
        if (otpService.verifyOTP(request.primaryContactEmail(), request.otp())){
            AppUser user = appUserRepository.findByPrimaryContactEmail(request.primaryContactEmail())
                    .orElseThrow(UserNotFound::new);

            String accessToken = jwtService.generateAccessToken(user.getPrimaryContactEmail(), user.getUserType(),user.getUserId());
            user.setIsVerified(true);

            appUserRepository.save(user);

            ResponseCookie jwtCookie = ResponseCookie.from("JWTAccess_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(3600)
                .build();
            response.setHeader("Set-Cookie", jwtCookie.toString());
            return new VerificationResponse(
                    true,
                    user.getUserType()
            );
        }else{
            throw new InvalidOTP();
        }
    }

    public AppUser verifyUser(VerifyUser verifyUser){
        Optional<AppUser> user = appUserRepository.findByPrimaryContactEmail(verifyUser.primaryContactEmail());
        if (user.isEmpty()){
            throw new UserNotFound();
        }else{
            AppUser appUser = user.get();
            appUser.setIsVerified(true);

            appUserRepository.save(appUser);
        }
        return user.get();
    }

    public void verify(VerifyUser user){
        Optional<AppUser> appUser = appUserRepository.findByPrimaryContactEmail(user.primaryContactEmail());
        if (appUser.isPresent()){
            AppUser appUser1 = appUser.get();
            String otp = otpService.generateAndStoreOTP((appUser1.getPrimaryContactEmail()));
            EmailRequest request = new EmailRequest(
                    appUser1.getPrimaryContactEmail(),
                    "Login OTP"
            );

            Context context = new Context();
            context.setVariable("otp", otp);
            emailService.sendEmail(request, "LoginTemplate", context);
        }else{
            throw new UserNotFound();
        }
    }



    public UploadFileResponse uploadFile(MultipartFile file) throws IOException {
        UploadFileResponse result = fileUploadService.uploadFile("test", file);
        System.out.println("file upload result: " + result.toString());
        return result;
    }

    private UploadFileResponse uploadInvestorFiles(String key, MultipartFile file) throws IOException {
        return fileUploadService.uploadFile(key, file);
    }


}

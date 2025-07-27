package com.arete.korbly.modules.shared.application;

import com.amazonaws.services.s3.AmazonS3;
import com.arete.korbly.infrastructure.integrations.S3FileUploadService;
import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.investor.domain.Investor;
import com.arete.korbly.modules.investor.persistence.InvestorRepository;
import com.arete.korbly.modules.shared.Mappers.InvestorMapper;
import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.dto.InvestorApplicationDTO;
import com.arete.korbly.modules.shared.dto.InvestorDTO;
import com.arete.korbly.modules.shared.enums.UploadFileResponse;
import com.arete.korbly.modules.shared.enums.UserType;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final JWTService jwtService;
    private final InvestorMapper investorMapper;
    private final InvestorRepository investorRepository;
    private final AmazonS3 amazonS3;
    private final S3FileUploadService fileUploadService;

    public AuthService(AppUserRepository appUserRepository,
                       JWTService jwtService, InvestorMapper investorMapper, InvestorRepository investorRepository, AmazonS3 amazonS3, S3FileUploadService fileUploadService) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
        this.investorMapper = investorMapper;
        this.investorRepository = investorRepository;
        this.amazonS3 = amazonS3;
        this.fileUploadService = fileUploadService;
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
        UploadFileResponse boardRes = uploadInvestorFiles(baseKey + "boardResolution", boardResolution);

        newInvestor.setCertificateOfIncorporationURL(incCert.key());
        newInvestor.setAuditedFinancialStatementsURL(invFile.key());
        newInvestor.setInvestmentPolicyStatementURL(policyStmt.key());
        newInvestor.setBoardResolutionURL(boardRes.key());

        appUserRepository.save(investor);
        investorRepository.save(newInvestor);
        return investorMapper.investorEntityToInvestorDTO(newInvestor);

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

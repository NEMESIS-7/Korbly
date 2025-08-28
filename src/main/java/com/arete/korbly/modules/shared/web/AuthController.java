package com.arete.korbly.modules.shared.web;

import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.investor.dto.InvestorApplicationDTO;
import com.arete.korbly.modules.shared.dto.VerificationRequest;
import com.arete.korbly.modules.shared.dto.VerifyUser;
import com.arete.korbly.modules.shared.service.AuthService;
import com.arete.korbly.modules.sme.dto.SMEApplicationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final JWTService jwtService;
    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;
    private final HttpServletResponse response;

    public AuthController(AuthService authService,
                          JWTService jwtService,
                          HttpServletRequest request, ObjectMapper objectMapper, HttpServletResponse response) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.request = request;
        this.objectMapper = objectMapper;
        this.response = response;
    }

    @PostMapping("/onboard-investor")
    public ResponseEntity<?> onboardInvestor(
            @RequestPart String investorApplicationDTO,
            @RequestPart MultipartFile certOfIncorporation,
            @RequestPart MultipartFile latestAuditedFinancialStatements,
            @RequestPart MultipartFile investmentPolicyStatement,
            @RequestPart MultipartFile boardResolution
    ) throws IOException {

        InvestorApplicationDTO investor = objectMapper.readValue(investorApplicationDTO, InvestorApplicationDTO.class);

        return new ResponseEntity<>(authService.onboardInvestor(
                investor,
                certOfIncorporation,
                latestAuditedFinancialStatements,
                investmentPolicyStatement,
                boardResolution
        ), HttpStatus.OK);
    }

    @PostMapping("/onboard-sme")
    public ResponseEntity<?> onboardSME(
            @RequestPart String smeApplicationDTO,
            @RequestPart MultipartFile certOfIncorporation,
            @RequestPart MultipartFile latestFinancialStatements,
            @RequestPart MultipartFile businessPlan,
            @RequestPart MultipartFile taxClearanceCert
    ) throws IOException {
        SMEApplicationDTO newSmeApplicationDTO = objectMapper.readValue(smeApplicationDTO, SMEApplicationDTO.class);

        return new ResponseEntity<>(authService.onboardSME(
                newSmeApplicationDTO,
                certOfIncorporation,
                latestFinancialStatements,
                businessPlan,
                taxClearanceCert
        ), HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyInvestorOrSME(@RequestBody VerificationRequest request){
        return new ResponseEntity<>(authService.verifyUserLogin(request, response), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody VerifyUser user){
        authService.verify(user);
        return new ResponseEntity<>("OTP sent to email", HttpStatus.OK);    }


    @PostMapping("/test-upload")
    public ResponseEntity<?> uploadFileTest(@RequestPart ("testFile") MultipartFile testUpload) throws IOException {
        return new ResponseEntity<>(authService.uploadFile(testUpload), HttpStatus.OK);
    }

    private boolean validateEmailString(String email){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return patternMatches(email,regexPattern);
    }

    private boolean patternMatches(String email, String regexPattern){
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}
package com.arete.korbly.modules.shared.web;

import com.arete.korbly.infrastructure.security.JWTService;
import com.arete.korbly.modules.shared.application.AuthService;
import com.arete.korbly.modules.shared.dto.InvestorApplicationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final JWTService jwtService;
    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;

    public AuthController(AuthService authService,
                          JWTService jwtService,
                          HttpServletRequest request, ObjectMapper objectMapper) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.request = request;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/investor-onboard")
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

    @PostMapping("/test-upload")
    public ResponseEntity<?> uploadFileTest(@RequestPart ("testFile") MultipartFile testUpload) throws IOException {
        return new ResponseEntity<>(authService.uploadFile(testUpload), HttpStatus.OK);
    }

}
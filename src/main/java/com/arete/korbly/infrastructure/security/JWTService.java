package com.arete.korbly.infrastructure.security;


import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.UserType;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {
    private String secretKey = System.getenv("JWT_SECRET");

    private final AppUserRepository appUserRepository;

    public JWTService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @PostConstruct
    private void validateSecretKey() {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("JWT_SECRET environment variable is not set. Application cannot start.");
        }
    }

    public String generateRefreshToken(String username, UserType userType, UUID userID) {
        long refreshTokenExp = 604800000L; // 7 days
        return generateToken(username, refreshTokenExp, userType, userID);
    }

    public String generateAccessToken(String username, UserType userType, UUID userId) {
        long accessTokenExpirationTime = 3600000L; // 1 hour
        return generateToken(username, accessTokenExpirationTime, userType, userId);
    }

    public String generateToken(String username, long expirationTime, UserType userType, UUID userId) {
        Optional<AppUser> appUser = appUserRepository.findById(userId);
        Map<String, Object> claims = new HashMap<>();
        if (appUser.isPresent()){
            AppUser user = appUser.get();
            claims.put("appUserEmail", user.getPrimaryContactEmail());
            claims.put("appUserId", user.getUserId());
            claims.put("appUserType", user.getUserType());

            if (user.getUserType().equals(UserType.REGULATORY_AUTHORITY)) {
                claims.put("regulatorUserId", user.getUserId());
            } else if (user.getUserType().equals(UserType.ADMIN)) {
                claims.put("adminUserId", user.getUserId());
            } else if (user.getUserType().equals(UserType.INSURANCE_REINSURANCE)) {
                claims.put("insurerUserId", user.getUserId());
            } else if (user.getUserType().equals(UserType.SME)) {
                claims.put("smeUserId", user.getUserId());
            } else if (user.getUserType().equals(UserType.INVESTOR)) {
                claims.put("investorUserId", user.getUserId());
            } else if (user.getUserType().equals(UserType.HNWI)) {
                claims.put("hnwiUserId", user.getUserId());
            }
        }

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .signWith(getKey())
                .compact();
    }


    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaim(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractRole(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("appUserRole", String.class);
    }


    public String extractUserEmail(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("appUserEmail", String.class);
    }

    public String extractUserType(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("appUserType", String.class);
    }

    public UUID extractAppUserId(String token) {
        Claims claims = extractAllClaim(token);
        if (claims.containsKey("appUserId")) {
            return UUID.fromString(claims.get("appUserId", String.class));
        }
        return null;
    }

    public String extractAppUserRole(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("appUserType", String.class);
    }

    public UUID extractAppUserId(HttpServletRequest request){
        String token = getTokenFromAuthorizationHeader(request);
        if (token == null) {
            token = getTokenFromCookie(request.getCookies());
        }
        if (token == null) {
            return null;
        }
        return extractAppUserId(token);
    }

    private String getTokenFromAuthorizationHeader(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private String getTokenFromCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWTAccess_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

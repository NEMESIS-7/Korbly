package com.arete.korbly.infrastructure.security;


import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.UserType;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    Dotenv dotenv = Dotenv.configure().load();
    private final String secretKey = dotenv.get("JWT_SECRET");

    private final AppUserRepository appUserRepository;

    public JWTService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public String generateRefreshToken(String username, UserType userType, UUID userID) {
        long refreshTokenExp = 15552000000L;
        return generateToken(username, refreshTokenExp, userType, userID);
    }

    public String generateAccessToken(String username, UserType userType, UUID userId) {
        long accessTokenExpirationTime = 15552000000L;
        return generateToken(username, accessTokenExpirationTime, userType, userId);
    }

    public String generateToken(String username, long expirationTime, UserType userType, UUID userId) {
        Optional<AppUser> appUser = appUserRepository.findById(userId);
        Map<String, Object> claims = new HashMap<>();
        if (appUser.isPresent()){
            claims.put("appUserEmail", appUser.get().getPrimaryContactEmail());
            claims.put("appUserId",appUser.get().getUserId());
            claims.put("appUserType", appUser.get().getUserType());
        }
        if(appUser.get().getUserType().equals(UserType.REGULATORY_AUTHORITY)){
            claims.put("regulatorUserId", appUser.get().getUserId());
        }
        if(appUser.get().getUserType().equals(UserType.ADMIN)){
            claims.put("adminUserId", appUser.get().getUserId());
        }
        if(appUser.get().getUserType().equals(UserType.INSURANCE_REINSURANCE)){
            claims.put("insurerUserId", appUser.get().getUserId());
        }
        if(appUser.get().getUserType().equals(UserType.SME)){
            claims.put("smeUserId", appUser.get().getUserId());
        }
        if(appUser.get().getUserType().equals(UserType.INVESTOR)){
            claims.put("investorUserId", appUser.get().getUserId());
        }
        if(appUser.get().getUserType().equals(UserType.HNWI)){
            claims.put("hnwiUserId", appUser.get().getUserId());
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
                .get("appUserId", String.class);
    }

    public UUID extractAppUserId(HttpServletRequest request){
        String token;
        token = getTokenFromCookie(request.getCookies());
        if(token != null){
            token = getTokenFromAuthorizationHeader(request);
        }
        return extractAppUserId(token);
    }

    private String getTokenFromAuthorizationHeader(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            System.out.println("auth header: " + authorizationHeader);
            return authorizationHeader.substring(7); // Remove "Bearer " prefix
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

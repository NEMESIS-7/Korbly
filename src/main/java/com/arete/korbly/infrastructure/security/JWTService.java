package com.arete.korbly.infrastructure.security;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.shared.enums.UserType;
import com.arete.korbly.modules.shared.persistence.AppUserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@ServiceI
public class JWTService {

    Dotenv dotenv = Dotenv.configure().load();
    private String secretKey = dotenv.get("JWT_SECRET");

    private final AppUserRepository appUserRepository;

    public JWTService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public String generateRefreshToken(String username, UserType role, UUID userID) {
        long refreshTokenExp = 15552000000L;
        return generateToken(username, refreshTokenExp, role, userID);
    }

    public String generateAccessToken(String username, UserType role, UUID userId) {
        long accessTokenExpirationTime = 15552000000L;
        return generateToken(username, accessTokenExpirationTime, role, userId);
    }

    public String generateToken(String username, long expirationTime, UserType role, UUID userId) {
        Optional<AppUser> appUser = appUserRepository.findById(userId);
        Map<String, Object> claims = new HashMap<>();
        if (appUser.isPresent()){
            claims.put("userEmail", appUser.get().getPrimaryContactEmail());
            claims.put("userId",appUser.get().getUserId());
            claims.put("userType", appUser.get().getUserType());
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
                .get("role", String.class);
    }


    public String extractUserEmail(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userEmail", String.class);
    }

    public String extractUserType(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public UUID extractUserId(String token) {
        Claims claims = extractAllClaim(token);
        if (claims.containsKey("userId")) {
            return UUID.fromString(claims.get("userId", String.class));
        }
        return null;
    }
}

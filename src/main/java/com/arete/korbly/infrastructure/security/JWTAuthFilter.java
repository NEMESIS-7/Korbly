package com.arete.korbly.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JWTAuthFilter(JWTService jwtService,
                         CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Try to get token from Authorization header first
        String token = getTokenFromAuthorizationHeader(request);
        System.out.println("token from auth header: " + token);

        // If no token in header, try to get from cookie
        if (token == null) {
            token = getTokenFromCookie(request.getCookies());
        }
        System.out.println("token from cookie/header: " + token);

        // If no token found anywhere, continue without authentication
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String userEmail = jwtService.extractUserEmail(token);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
                final String userType = jwtService.extractUserType(token);
                List<SimpleGrantedAuthority> userAuthorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userType));

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userAuthorities
                    );

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
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
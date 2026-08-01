package com.arete.korbly.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JWTAuthFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;
    private final AllRequestsLoggingFilter loggingFilter;

    public SecurityConfig(JWTAuthFilter jwtFilter, CustomUserDetailsService userDetailsService, AllRequestsLoggingFilter loggingFilter) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
        this.loggingFilter = loggingFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/auth/onboard-investor", "/api/v1/auth/onboard-sme",
                                         "/api/v1/auth/verify", "/api/v1/auth/v2/verify", "/api/v1/auth/login").permitAll()
                        // Actuator restricted to ADMIN only
                        .requestMatchers("/actuator/**").hasRole("ADMIN")

                        // Admin-only mutations
                        .requestMatchers(HttpMethod.POST, "/api/v1/regulator/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/regulator/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/credit/evaluate/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/credit/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/syndication/allocations/**").hasRole("ADMIN")

                        // Regulator + admin reads
                        .requestMatchers("/api/v1/regulator/**").hasAnyRole("REGULATORY_AUTHORITY", "ADMIN")

                        // Termsheet mutations — restrict destructive/versioning operations to ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/termsheets/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/termsheets/*/latest").hasRole("ADMIN")

                        // SME-only mutations
                        .requestMatchers(HttpMethod.POST, "/api/v1/syndication/create-deal").hasRole("SME")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/syndication/deals/delete/**").hasRole("SME")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/syndication/deals/next-stage/**").hasAnyRole("SME", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/syndication/tranche/create/**").hasAnyRole("SME", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/syndication/tranche/delete/**").hasAnyRole("SME", "ADMIN")
                        .requestMatchers("/api/v1/smes/**").hasRole("SME")

                        // Investor-only
                        .requestMatchers("/api/v1/investor/**").hasAnyRole("INVESTOR", "HNWI", "INSURANCE_REINSURANCE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/syndication/allocations/create").hasAnyRole("INVESTOR", "HNWI", "INSURANCE_REINSURANCE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/syndication/investor/deals").hasAnyRole("INVESTOR", "HNWI", "INSURANCE_REINSURANCE")

                        // All remaining routes require a valid JWT
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

}

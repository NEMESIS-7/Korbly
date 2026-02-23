package com.arete.korbly.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableWebSecurity(debug = true)
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
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
                                "/api/v1/auth/**", "/actuator/**")
                        .permitAll()
                        .requestMatchers("/api/v1/credit/**").permitAll()
                        .requestMatchers("/api/v1/investor/**").permitAll()
                        .requestMatchers("/api/v1/regulator/**").permitAll()
                        .requestMatchers("/api/v1/documents/**").permitAll()
                        .requestMatchers("/api/v1/smes/**").permitAll()
                        .requestMatchers("/api/v1/syndication/create-deal", "/api/v1/syndication/deals/get-deal/**",
                                "/api/v1/syndication/get-deals/**", "/api/v1/syndication/deals/delete/**", "/api/v1/syndication/tranche/create/**",
                                "/api/v1/syndication/tranche/delete/**", "/api/v1/syndication/tranches/get-all", "/api/v1/termsheets/tranche/**").permitAll()
                        .requestMatchers("/api/v1/syndication/allocations/**", "/api/v1/syndication/{trancheId}/allocations",
                                "/api/v1/syndication/{investorId}/allocation", "/api/v1/syndication/investor/deals",
                                "/api/v1/termsheets/**", "/api/v1/conditions-precedent/**", "/api/v1/valuation/**")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
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

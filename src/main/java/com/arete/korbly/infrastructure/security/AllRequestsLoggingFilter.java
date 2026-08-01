package com.arete.korbly.infrastructure.security;

import com.arete.korbly.infrastructure.domain.RequestLog;
import com.arete.korbly.infrastructure.persistence.RequestLogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@Component
public class AllRequestsLoggingFilter extends OncePerRequestFilter {
    private final RequestLogRepository logRepository;
    private final AsyncSaveUtil util;

    public AllRequestsLoggingFilter(RequestLogRepository logRepository,
                                    AsyncSaveUtil util) {
        this.logRepository = logRepository;
        this.util = util;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        RequestLog newLog = RequestLog
                .builder()
                .timestamp(Timestamp.from(Instant.now()))
                .method(request.getMethod())
                .uri(request.getRequestURI())
                .clientIp(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .statusCode(response.getStatus())
                .build();

        Long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        Long endTime = System.currentTimeMillis();

        newLog.setStatusCode(response.getStatus());
        newLog.setDurationMs(endTime - startTime);

        if(request.getUserPrincipal() != null){
            newLog.setUsername(request.getUserPrincipal().getName());
        }
        newLog.setReferer(request.getHeader("Referer"));

        util.asyncSave(newLog);
    }

}

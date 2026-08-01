package com.arete.korbly.infrastructure.domain;

import jakarta.persistence.*;
import lombok.Builder;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Builder
@Table(
        indexes = {
                @Index(name = "idx_method", columnList = "method"),
                @Index(name = "idx_status_code", columnList = "status_code")
        }
)
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Timestamp timestamp;
    private String method;
    private String uri;
    private String clientIp;
    private String userAgent;
    private Integer statusCode;
    private Long durationMs;

    // Only filled if request is authenticated
    private String username;
    private String roles;

    // Only filled if request is unauthenticated
    private String referer;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public RequestLog() {
    }

    public RequestLog(UUID id, Timestamp timestamp, String method, String uri, String clientIp, String userAgent, Integer statusCode, Long durationMs, String username, String roles, String referer) {
        this.id = id;
        this.timestamp = timestamp;
        this.method = method;
        this.uri = uri;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
        this.statusCode = statusCode;
        this.durationMs = durationMs;
        this.username = username;
        this.roles = roles;
        this.referer = referer;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }
}


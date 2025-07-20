package com.arete.korbly.modules.shared.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Getter
@Setter
public class  APIException {
    private final String status;
    private final int statusCode;
    private final APIError error;
    private final String requestId;

    public APIException(String status,
                        int statusCode,
                        APIError error,
                        String requestId) {
        this.status = status;
        this.statusCode = statusCode;
        this.error = error;
        this.requestId = requestId;
    }

    public record APIError(HttpStatus code,
                           String message,
                           Timestamp timestamp,
                           String apiPath) {

    }
}
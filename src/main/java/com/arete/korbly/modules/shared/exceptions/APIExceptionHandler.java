package com.arete.korbly.modules.shared.exceptions;


import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;

@ControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(InvestorNotFound.class)
    public ResponseEntity<?> handleInvestorAccountNotFound(InvestorNotFound e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.NOT_FOUND.value(),
                new APIException.APIError(
                        HttpStatus.NOT_FOUND,
                        "Investor account does not exist",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidEmail.class)
    public ResponseEntity<?> handleInvalidEmail(InvalidEmail e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.APIError(
                        HttpStatus.BAD_REQUEST,
                        "Email is invalid",
                        Timestamp.from(Instant.now()),
                        request.getRequestURI()
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}

package com.arete.korbly.modules.shared.exceptions;


import com.arete.korbly.modules.syndication.exceptions.*;
import com.arete.korbly.modules.termsheet.exceptions.InvalidUpdate;
import com.arete.korbly.modules.termsheet.exceptions.TermSheetNotFound;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
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
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTrancheUpdate.class)
    public ResponseEntity<?> handleInvalidTrancheUpdate(InvalidTrancheUpdate e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.APIError(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmail.class)
    public ResponseEntity<?> handleInvalidEmail(InvalidEmail e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.APIError(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<?> handleInvalidFormatException(InvalidFormatException e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.APIError(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDealUpdate.class)
    public ResponseEntity<?> handleInvalidDealUpdate(InvalidDealUpdate e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.APIError(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DealAmountExceeded.class)
    public ResponseEntity<?> handleDealAmountExceeded(DealAmountExceeded e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.APIError(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DealStatusUpdateException.class)
    public ResponseEntity<?> handleDealStatusUpdateException(DealStatusUpdateException e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.APIError(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOTP.class)
    public ResponseEntity<?> handleInvalidOTP(InvalidOTP e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.UNAUTHORIZED.value(),
                new APIException.APIError(
                        HttpStatus.UNAUTHORIZED,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFound e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.UNAUTHORIZED.value(),
                new APIException.APIError(
                        HttpStatus.UNAUTHORIZED,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<?> handlePSQLException(PSQLException e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid",
                HttpStatus.CONFLICT.value(),
                new APIException.APIError(
                        HttpStatus.CONFLICT,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SMENotFound.class)
    public ResponseEntity<?> handleSMENotFound(SMENotFound e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Not Found",
                HttpStatus.NOT_FOUND.value(),
                new APIException.APIError(
                        HttpStatus.NOT_FOUND,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidFinancials.class)
    public ResponseEntity<?> handleInvalidFinancials(InvalidFinancials e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Invalid financial data",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.APIError(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TermSheetNotFound.class)
    public ResponseEntity<?> handleTermSheetNotFound(TermSheetNotFound e, HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.APIError(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUpdate.class)
    public ResponseEntity<?> handleInvalidUpdate(InvalidUpdate e, HttpServletRequest request){
        APIException apiException = new APIException(
                "error",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.APIError(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Duplicate",
                HttpStatus.CONFLICT.value(),
                new APIException.APIError(
                        HttpStatus.CONFLICT,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DealNotFound.class)
    public ResponseEntity<?> handleDealNotFound(DealNotFound e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Error",
                HttpStatus.NOT_FOUND.value(),
                new APIException.APIError(
                        HttpStatus.NOT_FOUND,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(InvalidAllocationAmount.class)
    public ResponseEntity<?> handleInvalidAllocationAmount(InvalidAllocationAmount e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Error",
                HttpStatus.BAD_REQUEST.value(),
                new APIException.APIError(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedAccess.class)
    public ResponseEntity<?> handleUnauthorizedAccess(UnauthorizedAccess e, HttpServletRequest request){
        APIException apiException = new APIException(
                "Error",
                HttpStatus.UNAUTHORIZED.value(),
                new APIException.APIError(
                        HttpStatus.UNAUTHORIZED,
                        e.getMessage(),
                        Timestamp.from(Instant.now())
                ),
                request.getRequestId()
        );
        Sentry.setTag("requestId", request.getRequestId());
        Sentry.setExtra("path", request.getRequestURI());
        Sentry.captureException(e);
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }
}

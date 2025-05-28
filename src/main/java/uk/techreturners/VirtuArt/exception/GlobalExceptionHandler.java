package uk.techreturners.VirtuArt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Common keys for response body
    private static final String MESSAGE_UNKNOWN_SERVICE_ERROR = "Unknown service error";

    @ExceptionHandler(ApiServiceException.class)
    public ResponseEntity<Object> handleApiServiceError(ApiServiceException e) {
        HttpStatus status = e.getStatus() != null ? e.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = e.getMessage() != null ? e.getMessage() : MESSAGE_UNKNOWN_SERVICE_ERROR;

        return ResponseEntity.status(status).body(message);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
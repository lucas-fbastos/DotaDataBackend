package io.luck.AghsLabs.resource.exceptions;


import io.luck.AghsLabs.exception.ExternalApiException;
import io.luck.AghsLabs.exception.BadRequestException;
import io.luck.AghsLabs.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<StandardError> externalApiError(ExternalApiException e, HttpServletRequest req){
        StandardError error = new StandardError(HttpStatus.BAD_GATEWAY.value(),e.getMessage(),System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> badRequest(BadRequestException e){
        StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e){
        StandardError error = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e){
        StandardError error = new StandardError(HttpStatus.NOT_FOUND.value(),e.getMessage(),System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

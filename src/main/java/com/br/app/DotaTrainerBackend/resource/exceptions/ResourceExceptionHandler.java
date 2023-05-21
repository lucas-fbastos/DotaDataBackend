package com.br.app.DotaTrainerBackend.resource.exceptions;


import com.br.app.DotaTrainerBackend.exception.ExternalApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<StandardError> StandardError(ExternalApiException e, HttpServletRequest req){
        StandardError error = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage(),System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

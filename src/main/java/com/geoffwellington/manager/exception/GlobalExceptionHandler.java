package com.geoffwellington.manager.exception;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExceptionHandler
    public ResponseEntity<String> defaultHandler(RuntimeException exception, WebRequest request) {
        LOGGER.warn(exception.getMessage(), exception);
        return new ResponseEntity<>("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> missingResourceHandler(RuntimeException exception, WebRequest request) {
        LOGGER.warn(exception.getMessage(), exception);
        return new ResponseEntity<>("Resource not found.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<String> badRequestHandler(RuntimeException exception, WebRequest request) {
        LOGGER.warn(exception.getMessage(), exception);
        return new ResponseEntity<>("Check request format and retry.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> validationHandler(RuntimeException exception, WebRequest request) {
        LOGGER.warn(exception.getMessage(), exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

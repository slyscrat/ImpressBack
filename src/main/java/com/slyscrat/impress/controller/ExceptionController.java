package com.slyscrat.impress.controller;

import com.slyscrat.impress.exception.EntityNotFoundException;
import com.slyscrat.impress.exception.InnerLogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(InnerLogicException.class)
    public ResponseEntity<String> handleInnerLogicException(InnerLogicException exception) {
        String errorMessage = "Internal server error has occurred";
        LOGGER.error("Exception occurred in inner logic:", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException exception) {
        LOGGER.error("No such entity exists exception", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something went terribly wrong on the server side.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleOtherExceptions(RuntimeException exception) {
        String errorMessage = "Unfortunately, internal server error has occurred";
        LOGGER.error("Unpredicted internal server error has occurred", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}

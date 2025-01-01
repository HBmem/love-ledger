package com.hbmem.LoveLedger.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleException(JsonProcessingException ex) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse("Something went wrong trying to process json data."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

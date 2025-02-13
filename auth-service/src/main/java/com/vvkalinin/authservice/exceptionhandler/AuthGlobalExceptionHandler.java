package com.vvkalinin.authservice.exceptionhandler;

import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class AuthGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<BaseApiException> handleException(NoSuchElementException ex) {
        BaseApiException incorrectData = new BaseApiException(1, ex.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<BaseApiException> handleException(MongoWriteException ex) {
        BaseApiException incorrectData = new BaseApiException(2, ex.getError().getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.BAD_REQUEST);
    }

}

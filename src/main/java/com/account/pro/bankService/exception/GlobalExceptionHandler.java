package com.account.pro.bankService.exception;

import com.account.pro.bankService.controller.res.RestError;
import com.account.pro.bankService.controller.res.RestResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

// todo 알아보기.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // if
    @ExceptionHandler(BankRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public RestError handleBankRuntimeException(BankRuntimeException e) {
        return new RestError("bank_exception", e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public RestError handleNullPointerException(NullPointerException e){
        return new RestError("bad_request", e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public RestError handleNoHandlerFoundException(NoHandlerFoundException e){
        return new RestError("not_found", e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RestError handleMethodNotAllowed(HttpRequestMethodNotSupportedException e){
        return new RestError("method_not_allowed", e.getMessage());
    }

    // else
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public RestError handleBankRuntimeException(Exception e) {
        return new RestError("exception", e.getMessage());
    }


}

package com.account.pro.bankService.exception;

import com.account.pro.bankService.controller.res.RestError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// todo 알아보기.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // if
    @ExceptionHandler(BankRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public RestError handleBankRuntimeException(BankRuntimeException e) {
        return new RestError("bank_exception", e.getMessage());
    }

    // else
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public RestError handleBankRuntimeException(Exception e) {
        return new RestError("exception", e.getMessage());
    }
}

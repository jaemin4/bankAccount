package com.pro.exception;

import com.pro.response.RestError;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BankRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public RestError handleBankRuntimeException(BankRuntimeException e) {
        return new RestError("bank_exception", e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError handleNullPointerException(NullPointerException e){
        return new RestError("bad_request", e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError handleException(NoHandlerFoundException e){
        return new RestError("not_found", e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError handleMethodNotAllowed(HttpRequestMethodNotSupportedException e){
        return new RestError("method_not_allowed", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public RestError handleBankRuntimeException(Exception e) {
        // todo 500 에러가 발생하면 개발자가 제일 먼저 알아야 합니다.
        //  텔레그램 BotFather 를 이용해서 메시지를 보낼수있습니다. 이부분 추가해보시면 좋겠습니다.
        return new RestError("exception", e.getMessage());
    }


}

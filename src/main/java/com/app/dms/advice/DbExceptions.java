package com.app.dms.advice;

import org.springframework.http.HttpStatus;

public class DbExceptions extends ApiBaseException{
    public DbExceptions(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}

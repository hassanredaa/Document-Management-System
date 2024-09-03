package com.app.dms.advice;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiBaseException{
    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}

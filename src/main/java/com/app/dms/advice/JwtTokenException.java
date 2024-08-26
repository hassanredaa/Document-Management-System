package com.app.dms.advice;

import org.springframework.http.HttpStatus;

public class JwtTokenException extends ApiBaseException{

    public JwtTokenException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}

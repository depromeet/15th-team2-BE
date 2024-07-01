package com.depromeet.domain.auth.exception;

import com.depromeet.global.exception.ExceptionType;

public enum AuthErrorCode implements ExceptionType {
    LOGIN_FAILED(401, "login failed")
    ,ACCESS_TOKEN_NOT_EXISTS(401, "access_token not exists")
    ,REFRESH_TOKEN_NOT_MATCH(401, "refresh_token not match")
    ;

    private final int status;
    private final String message;

    AuthErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

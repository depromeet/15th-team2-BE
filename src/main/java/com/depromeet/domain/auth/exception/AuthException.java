package com.depromeet.domain.auth.exception;

import com.depromeet.global.exception.BaseException;
import com.depromeet.global.exception.ExceptionType;

public class AuthException extends BaseException {
    public AuthException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

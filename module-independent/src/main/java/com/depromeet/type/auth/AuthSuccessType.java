package com.depromeet.type.auth;

import com.depromeet.type.SuccessType;

public enum AuthSuccessType implements SuccessType {
    LOGIN_SUCCESS("AUTH_1", "로그인에 성공하였습니다"),
    REISSUE_ACCESS_TOKEN_SUCCESS("AUTH_2", "JWT ACCESS 토큰 발급에 성공하였습니다"),
    LOGOUT_SUCCESS("AUTH_3", "로그아웃에 성공하였습니다"),
    DELETE_ACCOUNT_SUCCESS("AUTH_4", "회원 탈퇴에 성공하였습니다");

    private final String code;

    private final String message;

    AuthSuccessType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

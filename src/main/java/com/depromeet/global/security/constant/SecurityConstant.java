package com.depromeet.global.security.constant;

public enum SecurityConstant {

    ACCESS_HEADER("Authentication")
    ,REFRESH_HEADER("refresh")
    ;

    private String value;

    SecurityConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

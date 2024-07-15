package com.depromeet.type.memory;

import com.depromeet.type.SuccessType;

public enum MemorySuccessType implements SuccessType {
    POST_RESULT_SUCCESS("MEMORY_1", "수영 기록 저장에 성공하였습니다");

    private final String code;

    private final String message;

    MemorySuccessType(String code, String message) {
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
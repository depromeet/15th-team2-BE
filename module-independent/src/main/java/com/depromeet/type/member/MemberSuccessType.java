package com.depromeet.type.member;

import com.depromeet.type.SuccessType;

public enum MemberSuccessType implements SuccessType {
    GET_SUCCESS("MEMBER_1", "멤버 조회에 성공하였습니다"),
    UPDATE_GOAL_SUCCESS("MEMBER_2", "멤버 목표 수정에 성공하였습니다"),
    GET_GOAL_SUCCESS("MEMBER_3", "멤버 목표 조회에 성공하였습니다"),
    UPDATE_NAME_SUCCESS("MEMBER_4", "멤버 이름 수정에 성공하였습니다"),
    UPDATE_GENDER_SUCCESS("MEMBER_5", "멤버 성별 수정에 성공하였습니다"),
    SEARCH_MEMBER_SUCCESS("MEMBER_6", "멤버 검색에 성공하였습니다"),
    UPDATE_SUCCESS("MEMBER_7", "멤버 정보 수정에 성공하였습니다"),
    GET_DETAIL_SUCCESS("MEMBER_8", "멤버 정보 조회에 성공하였습니다");

    private final String code;

    private final String message;

    MemberSuccessType(String code, String message) {
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

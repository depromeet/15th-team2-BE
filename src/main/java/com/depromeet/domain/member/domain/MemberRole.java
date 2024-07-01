package com.depromeet.domain.member.domain;

import com.depromeet.domain.member.exception.MemberException;
import com.depromeet.global.converter.AbstractCodedEnumConverter;
import com.depromeet.global.converter.CodedEnum;

import java.util.Arrays;

import static com.depromeet.domain.member.exception.MemberErrorCode.MEMBER_ROLE_NOT_FOUND;

public enum MemberRole implements CodedEnum<String> {

    USER("USER")
    ,ADMIN("ADMIN")
    ;

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }


    @Override
    public String getValue() {
        return this.value;
    }

    public static MemberRole findByValue(String value) {
        return Arrays.stream(MemberRole.values())
                .filter(m -> m.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new MemberException(MEMBER_ROLE_NOT_FOUND));
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<MemberRole, String> {
        public Converter() {super(MemberRole.class);}
    }
}

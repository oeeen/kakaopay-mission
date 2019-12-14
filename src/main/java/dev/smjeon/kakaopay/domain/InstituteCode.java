package dev.smjeon.kakaopay.domain;

import dev.smjeon.kakaopay.service.NotFoundInstituteException;

import java.util.Arrays;

public enum InstituteCode {
    NHUF("주택도시기금", "bnk001"), // national housing urban fund
    KB("국민은행", "bnk002"),
    WOORI("우리은행", "bnk003"),
    SHINHAN("신한은행", "bnk004"),
    CITI("한국시티은행", "bnk005"),
    HANA("하나은행", "bnk006"),
    NH_SH("농협은행/수협은행", "bnk007"),
    KEB("외환은행", "bnk008"),
    ETC("기타은행", "bnk999");

    private final String name;
    private final String code;

    InstituteCode(final String name, final String code) {
        this.name = name;
        this.code = code;
    }

    public static String getInstituteCodeByName(String name) {
        return Arrays.stream(InstituteCode.values())
                .filter(instituteCode -> instituteCode.getName().equals(name))
                .findFirst()
                .orElseThrow(NotFoundInstituteException::new)
                .getCode();
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}

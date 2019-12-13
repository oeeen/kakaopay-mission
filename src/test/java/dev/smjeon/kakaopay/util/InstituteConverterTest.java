package dev.smjeon.kakaopay.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InstituteConverterTest {

    @Test
    @DisplayName("문자열에서 처음으로 숫자나 (가 나올 때까지의 문자로 변경")
    void convert() {
        assertEquals(InstituteConverter.convert("주택기금123입니다"), "주택기금");
    }
}
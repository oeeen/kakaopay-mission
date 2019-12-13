package dev.smjeon.kakaopay.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmountTest {

    @Test
    @DisplayName("정상 Amount 생성(숫자와 ,로만 이루어 진 기금)")
    void validate() {
        assertDoesNotThrow(() -> new Amount("1,234"));
        assertDoesNotThrow(() -> new Amount("12345"));
    }

    @Test
    @DisplayName("비정상 amount(숫자와 , 제외한 다른 형태)")
    void invalidAmount() {
        assertThrows(NumberFormatException.class, () -> new Amount("123a"));
    }
}
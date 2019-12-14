package dev.smjeon.kakaopay.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AmountTest {

    @Test
    @DisplayName(", 포함과 미포함 생성 테스트")
    void create() {
        assertDoesNotThrow(() -> new Amount("12345"));
        assertDoesNotThrow(() -> new Amount("12,345"));
    }

    @Test
    @DisplayName("Amount 정상일 때 getAmount")
    void abnormalCase() {
        Amount amount = new Amount("12345");
        assertThat(amount.getAmount()).isEqualTo(12345);
    }

    @Test
    @DisplayName("Amount 에 ,가 포함된 후 getAmount")
    void abnormalCaseGet() {
        Amount amount = new Amount("12,345");
        assertThat(amount.getAmount()).isEqualTo(12345);
    }
}
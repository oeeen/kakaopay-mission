package dev.smjeon.kakaopay.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HousingFinanceApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName(".csv 파일에서 데이터를 읽어와 데이터베이스에 저장합니다.")
    void loadCsv() {
        webTestClient.post()
                .uri("/prices")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$..AffectedRows").isEqualTo(1);
    }
}

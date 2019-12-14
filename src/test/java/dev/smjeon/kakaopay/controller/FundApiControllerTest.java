package dev.smjeon.kakaopay.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient(timeout = "200000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FundApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Timeout(value = 20)
    @DisplayName(".csv 파일에서 데이터를 읽어와 데이터베이스에 저장합니다.")
    @DirtiesContext
    void loadCsv() {
        readCsv().expectBody()
                .jsonPath("$..AffectedRows").isNotEmpty();
    }

    @Test
    @DisplayName("주택금융 공급 금융기관(은행) 목록을 출력합니다.")
    void listAllInstitutes() {
        webTestClient.get()
                .uri("/institutes")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$..name").isEqualTo("테스트은행")
                .jsonPath("$..code").isEqualTo("test123");
    }

    @Test
    @DisplayName("년도별 각 금융기관의 지원금액 합계를 출력합니다.")
    @DirtiesContext
    void totalAmountByYear() {
        readCsv();

        webTestClient.get()
                .uri("/years")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.[0].year").isEqualTo("2005")
                .jsonPath("$.[0].totalAmount").isEqualTo(48016)
                .jsonPath("$.[0].detailAmount.주택도시기금").isEqualTo(22247);
    }

    @Test
    @DisplayName("전체 지원 금액 중에서 가장 큰 금액의 기관명을 출력합니다.")
    @DirtiesContext
    void getInstituteByMaxAmount() {
        readCsv();

        webTestClient.get()
                .uri("/maxfund")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$..year").isEqualTo("2014")
                .jsonPath("$..instituteName").isEqualTo("주택도시기금");
    }

    WebTestClient.ResponseSpec readCsv() {
        ClassPathResource classPathResource = new ClassPathResource("input.csv");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        HttpEntity<ClassPathResource> entity = new HttpEntity<>(classPathResource, headers);

        return webTestClient.post()
                .uri("/amount")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", entity))
                .exchange()
                .expectStatus()
                .isOk();
    }
}
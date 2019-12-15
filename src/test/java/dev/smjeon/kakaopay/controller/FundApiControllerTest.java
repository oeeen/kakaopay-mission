package dev.smjeon.kakaopay.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.File;
import java.io.IOException;

@AutoConfigureWebTestClient(timeout = "200000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FundApiControllerTest {
    private static final String TOKEN_ID = "tokenId";
    private static final String TOKEN_PASSWORD = "tokenPassword";
    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Timeout(value = 20)
    @DisplayName(".csv 파일에서 데이터를 읽어와 데이터베이스에 저장합니다.")
    @DirtiesContext
    void loadCsv() throws IOException {
        readCsv().expectBody()
                .jsonPath("$..AffectedRows").isNotEmpty();
    }

    @Test
    @DisplayName("주택금융 공급 금융기관(은행) 목록을 출력합니다.")
    void listAllInstitutes() {
        String token = getToken(TOKEN_ID, TOKEN_PASSWORD);
        webTestClient.get()
                .uri("/institutes")
                .header(AUTHORIZATION, token)
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
    void totalAmountByYear() throws IOException {
        readCsv();

        String token = getToken(TOKEN_ID, TOKEN_PASSWORD);
        webTestClient.get()
                .uri("/years")
                .header(AUTHORIZATION, token)
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
    void getInstituteByMaxAmount() throws IOException {
        readCsv();

        String token = getToken(TOKEN_ID, TOKEN_PASSWORD);
        webTestClient.get()
                .uri("/maxfund")
                .header(AUTHORIZATION, token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$..year").isEqualTo("2014")
                .jsonPath("$..instituteName").isEqualTo("주택도시기금");
    }

    @Test
    @DisplayName("외환은행의 평균 지원 금액 중 최소값, 최대값을 출력합니다.")
    @DirtiesContext
    void findKEBAverageMinMax() throws IOException {
        readCsv();

        String token = getToken(TOKEN_ID, TOKEN_PASSWORD);
        webTestClient.get()
                .uri("/average")
                .header(AUTHORIZATION, token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.maximum.year").isEqualTo("2015")
                .jsonPath("$.minimum.year").isEqualTo("2017")
                .jsonPath("$.maximum.amount").isEqualTo(1702L)
                .jsonPath("$.minimum.amount").isEqualTo(0L);
    }

    @Test
    @DisplayName("국민은행의 2018년 2월의 지원금액 예측값")
    @DirtiesContext
    void predict() throws IOException {
        readCsv();

        String token = getToken(TOKEN_ID, TOKEN_PASSWORD);
        webTestClient.get()
                .uri("/predict?instituteName=국민은행&month=2")
                .header(AUTHORIZATION, token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.instituteName").isEqualTo("국민은행")
                .jsonPath("$.year").isEqualTo("2018")
                .jsonPath("$.month").isEqualTo("FEBRUARY")
                .jsonPath("$.amount.amount").isEqualTo(4849);
    }

    WebTestClient.ResponseSpec readCsv() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("input.csv");
        File file = classPathResource.getFile();

        return webTestClient.post()
                .uri("/amount")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", new FileSystemResource(file)))
                .exchange()
                .expectStatus()
                .isOk();
    }

    private String getToken(String userId, String userPassword) {
        return webTestClient.post()
                .uri("/api/signin")
                .body(BodyInserters.fromFormData("userId", userId)
                        .with("userPassword", userPassword))
                .exchange()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();
    }
}
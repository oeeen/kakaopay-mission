package dev.smjeon.kakaopay.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FundApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName(".csv 파일에서 데이터를 읽어와 데이터베이스에 저장합니다.")
    void loadCsv() {
        ClassPathResource classPathResource = new ClassPathResource("input.csv");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        HttpEntity<ClassPathResource> entity = new HttpEntity<>(classPathResource, headers);

        webTestClient.post()
                .uri("/amount")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", entity))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$..AffectedRows").isNotEmpty();
    }
}

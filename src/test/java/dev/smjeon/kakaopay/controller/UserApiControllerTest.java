package dev.smjeon.kakaopay.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void signUp() {
        webTestClient.post()
                .uri("/api/signup")
                .body(BodyInserters.fromFormData("userId", "TestUser")
                        .with("userPassword", "password"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$..userId").isEqualTo("TestUser");
    }
}
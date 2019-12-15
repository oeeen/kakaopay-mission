package dev.smjeon.kakaopay.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("회원 가입 후 응답 확인")
    void signUpResponseCheck() {
        signUp("TestUser", "TestPassword")
                .isOk()
                .expectBody()
                .jsonPath("$..userId").isEqualTo("TestUser");
    }

    @Test
    @DisplayName("로그인 확인")
    void signIn() {
        signUp("Martin", "password");

        webTestClient.post()
                .uri("/api/signin")
                .body(BodyInserters.fromFormData("userId", "Martin")
                .with("userPassword", "password"))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("중복 아이디로 가입 시도")
    void signUpDuplicatedUser() {
        signUp("sameUser", "password");
        signUp("sameUser", "password")
                .isBadRequest()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body).contains("이미 존재하는 아이디입니다.");
                });
    }

    @Test
    @DisplayName("잘못된 패스워드로 로그인 시도")
    void signInWrongPassword() {
        signUp("wrongUser", "password");
        webTestClient.post()
                .uri("/api/signin")
                .body(BodyInserters.fromFormData("userId", "wrongUser")
                        .with("userPassword", "wrongPassword"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body).contains("잘못된 패스워드입니다.");
                });
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 로그인 시도")
    void signInNotExistUser() {
        webTestClient.post()
                .uri("/api/signin")
                .body(BodyInserters.fromFormData("userId", "notExistUser")
                        .with("userPassword", "password"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(res.getResponseBody());
                    assertThat(body).contains("존재하지 않는 유저입니다.");
                });
    }

    private StatusAssertions signUp(String userId, String userPassword) {
        return webTestClient.post()
                .uri("/api/signup")
                .body(BodyInserters.fromFormData("userId", userId)
                        .with("userPassword", userPassword))
                .exchange()
                .expectStatus();
    }
}
package dev.smjeon.kakaopay.controller;

import dev.smjeon.kakaopay.dto.UserRequestDto;
import dev.smjeon.kakaopay.dto.UserResponseDto;
import dev.smjeon.kakaopay.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

    public UserApiController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.save(userRequestDto);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(UserRequestDto userRequestDto, HttpServletResponse response) {
        String token = userService.login(userRequestDto);
        response.setHeader("Authorization", token);
        return ResponseEntity.ok().body(token);
    }
}

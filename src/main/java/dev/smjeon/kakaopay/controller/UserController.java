package dev.smjeon.kakaopay.controller;

import dev.smjeon.kakaopay.dto.UserRequestDto;
import dev.smjeon.kakaopay.dto.UserResponseDto;
import dev.smjeon.kakaopay.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.save(userRequestDto);
        return ResponseEntity.ok().body(userResponseDto);
    }
}

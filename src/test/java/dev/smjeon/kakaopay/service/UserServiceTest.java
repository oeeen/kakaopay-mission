package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.User;
import dev.smjeon.kakaopay.domain.UserRepository;
import dev.smjeon.kakaopay.dto.UserRequestDto;
import dev.smjeon.kakaopay.dto.UserResponseDto;
import dev.smjeon.kakaopay.util.PasswordEncryptors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncryptors passwordEncryptors;

    @Mock
    private User user;

    @Mock
    private User foundUser;

    @Test
    void save() {
        UserRequestDto userRequestDto = new UserRequestDto("userId", "password");
        given(passwordEncryptors.encrypt(any(String.class))).willReturn(userRequestDto.getUserPassword());
        given(jwtService.generateToken(any(String.class))).willReturn("token");
        given(userRepository.save(any(User.class))).willReturn(user);
        given(user.getId()).willReturn(1L);
        given(user.getUserId()).willReturn("userId");

        UserResponseDto saved = userService.save(userRequestDto);

        assertThat(saved.getToken()).isEqualTo("token");
        assertThat(saved.getUserId()).isEqualTo(userRequestDto.getUserId());
        verify(userRepository).existsByUserId(userRequestDto.getUserId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login() {
        UserRequestDto userRequestDto = new UserRequestDto("userId", "password");
        given(userRepository.findByUserId(any(String.class))).willReturn(Optional.of(foundUser));
        given(foundUser.getUserPassword()).willReturn("password");
        given(jwtService.generateToken(any(String.class))).willReturn("token");
        given(passwordEncryptors.checkPassword(
                userRequestDto.getUserPassword(), foundUser.getUserPassword())).willReturn(true);

        String loginToken = userService.login(userRequestDto);

        assertThat(loginToken).isEqualTo("token");
        verify(userRepository).findByUserId("userId");
    }
}
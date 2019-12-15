package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.User;
import dev.smjeon.kakaopay.domain.UserRepository;
import dev.smjeon.kakaopay.dto.UserRequestDto;
import dev.smjeon.kakaopay.dto.UserResponseDto;
import dev.smjeon.kakaopay.service.exception.NotFoundUserException;
import dev.smjeon.kakaopay.service.exception.WrongPasswordException;
import dev.smjeon.kakaopay.util.PasswordEncryptors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncryptors passwordEncryptors;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncryptors passwordEncryptors) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncryptors = passwordEncryptors;
    }

    @Transactional
    public UserResponseDto save(UserRequestDto userRequestDto) {
        String afterPassword = passwordEncryptors.encrypt(userRequestDto.getUserPassword());
        User user = new User(userRequestDto.getUserId(), afterPassword);
        String token = jwtService.generateToken(userRequestDto);

        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser.getId(), savedUser.getUserId(), token);
    }

    public String login(UserRequestDto userRequestDto) {
        String userId = userRequestDto.getUserId();
        String userPassword = userRequestDto.getUserPassword();

        User foundUser = userRepository.findByUserId(userId)
                .orElseThrow(NotFoundUserException::new);

        if (!isSamePassword(userPassword, foundUser.getUserPassword())) {
            throw new WrongPasswordException();
        }

        return jwtService.generateToken(userRequestDto);
    }

    private boolean isSamePassword(String requestPassword, String encryptedPassword) {
        return passwordEncryptors.checkPassword(requestPassword, encryptedPassword);
    }
}

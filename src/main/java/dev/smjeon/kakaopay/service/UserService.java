package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.User;
import dev.smjeon.kakaopay.domain.UserRepository;
import dev.smjeon.kakaopay.dto.UserRequestDto;
import dev.smjeon.kakaopay.dto.UserResponseDto;
import dev.smjeon.kakaopay.util.PasswordEncryptors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public UserService(final UserRepository userRepository, final JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserResponseDto save(UserRequestDto userRequestDto) {
        String afterPassword = PasswordEncryptors.encrypt(userRequestDto.getUserPassword());
        User user = new User(userRequestDto.getUserId(), afterPassword);
        String token = jwtService.signUp(userRequestDto);

        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser.getId(), savedUser.getUserId(), token);
    }
}

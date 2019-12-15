package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.User;
import dev.smjeon.kakaopay.domain.UserRepository;
import dev.smjeon.kakaopay.dto.UserRequestDto;
import dev.smjeon.kakaopay.dto.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto save(UserRequestDto userRequestDto) {
        User user = new User(userRequestDto.getUserId(), userRequestDto.getUserPassword());
        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser.getId(), savedUser.getUserId());
    }
}

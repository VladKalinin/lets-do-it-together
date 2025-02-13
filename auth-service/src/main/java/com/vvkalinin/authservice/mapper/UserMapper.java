package com.vvkalinin.authservice.mapper;

import com.vvkalinin.authservice.dto.UserRequestDto;
import com.vvkalinin.authservice.dto.UserResponseDto;
import com.vvkalinin.authservice.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserMapper {

    public UserResponseDto toResponseDTO(User user) {
        return new UserResponseDto(
                user.id(),
                user.username(),
                user.email(),
                user.role()
        );
    }

    public User toEntity(UserRequestDto userCreateDTO, String hashedPassword) {
        return new User(
                UUID.randomUUID(),
                userCreateDTO.username(),
                hashedPassword,
                userCreateDTO.email(),
                userCreateDTO.role(),
                Instant.now(),
                Instant.now()
        );
    }

}

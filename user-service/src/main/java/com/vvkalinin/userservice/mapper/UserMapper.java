package com.vvkalinin.userservice.mapper;

import com.vvkalinin.userservice.dto.UpdateUserRequestDto;
import com.vvkalinin.userservice.dto.UserProfileDto;
import com.vvkalinin.userservice.dto.UserResponseDto;
import com.vvkalinin.userservice.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserResponseDto toResponseDTO(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    public UserProfileDto toProfileDto(User user) {
        return new UserProfileDto(
                user.getUsername()
        );
    }

}

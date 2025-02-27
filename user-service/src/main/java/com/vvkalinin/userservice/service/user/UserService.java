package com.vvkalinin.userservice.service.user;

import com.vvkalinin.userservice.dto.UpdateUserRequestDto;
import com.vvkalinin.userservice.dto.UserProfileDto;
import com.vvkalinin.userservice.dto.UserResponseDto;
import com.vvkalinin.userservice.model.User;
import org.apache.kafka.common.protocol.types.Field;

import java.util.UUID;

public interface UserService {

    UserResponseDto getCurrentUser();

    UserProfileDto getUserProfile(UUID userId);

    UserResponseDto updateUser(UUID userId, UpdateUserRequestDto userRequestDto);

    void deleteUser(UUID userId);

}

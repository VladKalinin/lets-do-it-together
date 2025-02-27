package com.vvkalinin.userservice.dto;

import com.vvkalinin.userservice.enums.Role;

import java.util.UUID;

public record  UserResponseDto(
        UUID id,
        String username,
        String email,
        Role role) {
}

package com.vvkalinin.authservice.dto;

import com.vvkalinin.authservice.enums.Role;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,
        Role role) {

}

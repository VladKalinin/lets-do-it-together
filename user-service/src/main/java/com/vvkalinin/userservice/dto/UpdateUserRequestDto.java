package com.vvkalinin.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UpdateUserRequestDto(
        @NotBlank String username,
        @NotBlank @Email String email) {
}

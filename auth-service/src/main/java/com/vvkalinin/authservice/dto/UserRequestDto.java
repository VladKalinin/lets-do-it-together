package com.vvkalinin.authservice.dto;

import com.vvkalinin.authservice.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDto(

        @NotBlank String username,
        @NotBlank @Email String email,
        @NotBlank String password, // Raw password (hashed before saving)
        @NotNull Role role) {

}
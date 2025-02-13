package com.vvkalinin.authservice.dto;

public record LoginRequestDto(
        String username,
        String password
) {
}

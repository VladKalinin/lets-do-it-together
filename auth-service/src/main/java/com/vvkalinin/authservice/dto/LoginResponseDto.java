package com.vvkalinin.authservice.dto;

public record LoginResponseDto(
        String accessToken,
        String refreshToken
) {
}

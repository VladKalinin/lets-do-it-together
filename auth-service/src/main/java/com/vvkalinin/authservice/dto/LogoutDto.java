package com.vvkalinin.authservice.dto;

public record LogoutDto(
        String revokeAccessToken,
        String revokeRefreshToken
) {

}

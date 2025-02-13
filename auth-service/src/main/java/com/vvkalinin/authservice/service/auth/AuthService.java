package com.vvkalinin.authservice.service.auth;

import com.vvkalinin.authservice.dto.LoginResponseDto;
import com.vvkalinin.authservice.dto.LogoutDto;
import com.vvkalinin.authservice.dto.RefreshRequestDto;
import com.vvkalinin.authservice.dto.UserRequestDto;
import com.vvkalinin.authservice.dto.UserResponseDto;
import com.vvkalinin.authservice.dto.LoginRequestDto;

public interface AuthService {

    UserResponseDto createUser(UserRequestDto user);

    LoginResponseDto login(LoginRequestDto loginRequest);

    LoginResponseDto refreshToken(RefreshRequestDto refreshRequest);

    void logout(LogoutDto logoutDto);

}

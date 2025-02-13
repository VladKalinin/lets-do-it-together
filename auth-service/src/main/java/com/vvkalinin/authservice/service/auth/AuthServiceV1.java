package com.vvkalinin.authservice.service.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.vvkalinin.authservice.dto.LoginResponseDto;
import com.vvkalinin.authservice.dto.LogoutDto;
import com.vvkalinin.authservice.dto.RefreshRequestDto;
import com.vvkalinin.authservice.dto.UserRequestDto;
import com.vvkalinin.authservice.dto.UserResponseDto;
import com.vvkalinin.authservice.jwt.JwtService;
import com.vvkalinin.authservice.mapper.UserMapper;
import com.vvkalinin.authservice.dto.LoginRequestDto;
import com.vvkalinin.authservice.model.User;
import com.vvkalinin.authservice.repository.UserRepository;
import com.vvkalinin.authservice.service.redis.TokenBlackListService;
import com.vvkalinin.authservice.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Qualifier("v1")
@AllArgsConstructor
public class AuthServiceV1 implements AuthService {
    private UserService userService;
    private TokenBlackListService tokenBlackListService;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponseDto createUser(UserRequestDto userCreateDTO) {
        String hashedPassword = passwordEncoder.encode(userCreateDTO.password());
        User user = userMapper.toEntity(userCreateDTO, hashedPassword);
        return userMapper.toResponseDTO(userService.create(user));
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.username(), loginRequest.password()));
        User user = (User) authentication.getPrincipal();
        var jwt = jwtService.generateToken(user);
        var refreshJwt = jwtService.generateRefreshToken(user);
        return new LoginResponseDto(jwt, refreshJwt);
    }

    @Override
    public LoginResponseDto refreshToken(RefreshRequestDto refreshRequest) {
        if (!tokenBlackListService.isTokenBlacklisted(refreshRequest.refreshToken())) {
            var user = userRepository.findByUsername(jwtService.validateRefreshToken(refreshRequest.refreshToken())).orElseThrow();
            var jwt = jwtService.generateToken(user);
            var refreshJwt = jwtService.generateRefreshToken(user);
            return new LoginResponseDto(jwt, refreshJwt);
        } else {
            throw new JWTVerificationException("Token is blacklisted");
        }
    }

    @Override
    public void logout(LogoutDto logoutDto) {
        tokenBlackListService.blacklistToken(logoutDto.revokeAccessToken());
        tokenBlackListService.blacklistToken(logoutDto.revokeRefreshToken());
    }

}

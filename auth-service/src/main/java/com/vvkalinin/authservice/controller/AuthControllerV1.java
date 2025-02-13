package com.vvkalinin.authservice.controller;

import com.vvkalinin.authservice.dto.LoginResponseDto;
import com.vvkalinin.authservice.dto.LogoutDto;
import com.vvkalinin.authservice.dto.RefreshRequestDto;
import com.vvkalinin.authservice.dto.UserRequestDto;
import com.vvkalinin.authservice.dto.UserResponseDto;
import com.vvkalinin.authservice.dto.LoginRequestDto;
import com.vvkalinin.authservice.exceptionhandler.BaseApiException;
import com.vvkalinin.authservice.model.User;
import com.vvkalinin.authservice.service.auth.AuthService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Qualifier("v1")
@AllArgsConstructor
@OpenAPIDefinition(info = @Info(
        title = "AUTH api",
        version = "1.0",
        description = "API for user authentication"
))
public class AuthControllerV1 {

    private final AuthService authService;

    @Operation(summary = "Register user")
    @ApiResponse(responseCode = "201", description = "User created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto user = authService.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "Login user")
    @ApiResponse(responseCode = "200", description = "User logged in",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "There is no such user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseApiException.class)))
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        var jwt = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(jwt);
    }

    @Operation(summary = "Refresh user toke")
    @ApiResponse(responseCode = "200", description = "Token refreshed",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class)))
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@Valid @RequestBody RefreshRequestDto refreshRequest) {
        var jwt = authService.refreshToken(refreshRequest);
        return ResponseEntity.status(HttpStatus.OK).body(jwt);
    }

    @Operation(summary = "Logout user")
    @ApiResponse(responseCode = "204", description = "User logged out")
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@Valid @RequestBody LogoutDto logoutDto) {
        authService.logout(logoutDto);
        return ResponseEntity.noContent().build();
    }

}

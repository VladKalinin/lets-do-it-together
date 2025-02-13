package com.vvkalinin.authservice.controller;

import com.vvkalinin.authservice.model.User;
import com.vvkalinin.authservice.service.user.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/me")
@Qualifier("v1")
@AllArgsConstructor
@OpenAPIDefinition(info = @Info(
        title = "Me api",
        version = "1.0",
        description = "API for get current user details"
))
public class MeControllerV1 {

    private final UserService userService;

    @Operation(summary = "Get current user", description = "Get current user by AUTH header")
    @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @GetMapping
    public ResponseEntity<User> getMe() {
        var user = userService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}

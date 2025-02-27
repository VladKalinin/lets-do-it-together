package com.vvkalinin.userservice.controller;

import com.vvkalinin.userservice.dto.UserProfileDto;
import com.vvkalinin.userservice.dto.UpdateUserRequestDto;
import com.vvkalinin.userservice.dto.UserResponseDto;
import com.vvkalinin.userservice.service.user.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@Qualifier("v1")
@AllArgsConstructor
@OpenAPIDefinition(info = @Info(
        title = "Users api",
        version = "1.0",
        description = "API to handle user details"
))
public class UsersControllerV1 {

    //TODO: extend logic of user details.
    //TODO: extend with roles in scope of preparation events-service
    //TODO: add get all api users
    //TODO: add fixed table with interests

    private final UserService userService;

    @Operation(summary = "Get current user", description = "Get current user from header")
    @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> retrieveMe() {
        var user = userService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> retrieveUserById(@PathVariable("userId") UUID userId) {
        var userProfile = userService.getUserProfile(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userProfile);
    }

    //TODO: after updating user details handle token strategy to prepare.
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("userId") UUID userId, @RequestBody UpdateUserRequestDto userRequestDto) {
        var user = userService.updateUser(userId,userRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    // TODO: should be performed by admin also
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable("userId") UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}

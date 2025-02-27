package com.vvkalinin.userservice.service.user;

import com.vvkalinin.userservice.dto.UpdateUserRequestDto;
import com.vvkalinin.userservice.dto.UserProfileDto;
import com.vvkalinin.userservice.dto.UserResponseDto;
import com.vvkalinin.userservice.mapper.UserMapper;
import com.vvkalinin.userservice.model.User;
import com.vvkalinin.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Qualifier("v1")
@AllArgsConstructor
public class UserServiceV1Impl implements UserService {

    private UserRepository repository;
    public UserMapper userMapper;

    public UserResponseDto getCurrentUser() {
        var username = currentUsername();
        return userMapper.toResponseDTO(getByUsername(username));
    }

    public UserProfileDto getUserProfile(UUID userId) {
        var user = getUserById(userId);
        return userMapper.toProfileDto(user);
    }

    @Override
    public UserResponseDto updateUser(UUID userId, UpdateUserRequestDto userRequestDto) {
        var user = getUserById(userId);
        user.setUsername(userRequestDto.username());
        user.setEmail(userRequestDto.email());
        user.setUpdatedAt(Instant.now());
        var updatedUser = repository.save(user);
        return userMapper.toResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(UUID userId) {
        var user = getUserById(userId);
        if (!currentUsername().equals(user.getUsername())) {
            throw new UsernameNotFoundException("Username not found");
        }
        repository.deleteById(userId);
    }

    private User getUserById(UUID userId) {
        return repository.findById(userId).orElseThrow();
    }

    private User getByUsername(String username) {
        return repository.findByUsername(username).orElseThrow();
    }

    private String currentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}

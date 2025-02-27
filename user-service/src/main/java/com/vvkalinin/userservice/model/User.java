package com.vvkalinin.userservice.model;

import com.vvkalinin.userservice.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "users")
public class User {

    private @Id UUID id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private @CreatedDate Instant createdAt;
    private @LastModifiedDate Instant updatedAt;

    public User(UUID id, String username, String email, String password, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}

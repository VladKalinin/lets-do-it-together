package com.vvkalinin.authservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vvkalinin.authservice.enums.Role;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Document(collection = "users")
public record User(
        @Id UUID id,
        @Getter @Indexed(unique = true) String username,
        @JsonIgnore @Getter String password,
        @Indexed(unique = true)
        String email,
        Role role,
        Instant createdAt,
        Instant updatedAt
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

}

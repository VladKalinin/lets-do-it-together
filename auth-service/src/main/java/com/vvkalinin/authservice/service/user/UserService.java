package com.vvkalinin.authservice.service.user;

import com.vvkalinin.authservice.model.User;
import com.vvkalinin.authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User create(User user) {
        return repository.save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username).orElseThrow();
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

}

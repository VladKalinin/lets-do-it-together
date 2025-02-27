package com.vvkalinin.userservice.service.filter;

import static java.util.Collections.emptyList;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FilterService extends OncePerRequestFilter {

    @Value("${spring.application.name}")
    private String forwardedFor;

    @Value("${vvkalinin.gateway.token}")
    private String gatewayToken;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                         @NonNull HttpServletResponse response,
                         @NonNull FilterChain filterChain) throws IOException, ServletException {

        var gatewayHost = request.getHeader("X-Forwarded-For");
        if (gatewayHost == null || !gatewayHost.contains(forwardedFor)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = request.getHeader("X-Forwarded-Token");
        if (token == null || !token.contains(gatewayToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        var username = request.getHeader("X-Auth-Username");
        if (username != null && !username.isEmpty()) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }

}

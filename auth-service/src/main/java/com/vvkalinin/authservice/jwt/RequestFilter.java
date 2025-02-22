package com.vvkalinin.authservice.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestFilter extends OncePerRequestFilter {

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
            requestIsNotAuthorized(response);
            return;
        }

        var token = request.getHeader("X-Forwarded-Token");
        if (token == null || !token.contains(gatewayToken)) {
            requestIsNotAuthorized(response);
            return;
        }

        filterChain.doFilter(request, response);

    }

    private void requestIsNotAuthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized access\"}");
        response.getWriter().flush();
    }

}

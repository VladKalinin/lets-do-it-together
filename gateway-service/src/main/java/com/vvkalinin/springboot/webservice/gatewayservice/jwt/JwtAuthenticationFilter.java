package com.vvkalinin.springboot.webservice.gatewayservice.jwt;

import com.vvkalinin.springboot.webservice.gatewayservice.service.TokenBlacklistService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter implements GatewayFilter {

    private JwtService jwtService;
    public TokenBlacklistService tokenBlacklistService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        var request = exchange.getRequest();

        if (request.getURI().getPath().startsWith("/api/v1/auth") ||
                request.getURI().getPath().startsWith("/api/auth/swagger-ui") ||
                request.getURI().getPath().startsWith("/api/users/swagger-ui")) {
            return chain.filter(exchange);
        }

        var authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorizedResponse(exchange, "Missing or invalid Authorization header");
        }

        try {
            var token = authHeader.substring(7);

            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                return unauthorizedResponse(exchange, "Token is blacklisted");
            }

            var jwt = jwtService.validateToken(token);
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-Auth-Username", jwt.getSubject())
                    .header("X-User-Email", jwt.getClaim("email").asString())
                    .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            return unauthorizedResponse(exchange,  "Invalid token: " + e.getMessage());
        }
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

}

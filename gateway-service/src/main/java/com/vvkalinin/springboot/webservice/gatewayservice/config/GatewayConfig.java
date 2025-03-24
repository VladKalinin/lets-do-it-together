package com.vvkalinin.springboot.webservice.gatewayservice.config;

import com.vvkalinin.springboot.webservice.gatewayservice.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final String gatewayToken;

    private final String gatewayAuth;

    private final String gatewayAuthHost;

    private final String gatewayUser;

    private final String gatewayUserHost;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public GatewayConfig(
            @Value("${vvkalinin.gateway.token}") String gatewayToken,
            @Value("${vvkalinin.gateway.auth}") String gatewayAuth,
            @Value("${vvkalinin.gateway.auth.host}") String gatewayAuthHost,
            @Value("${vvkalinin.gateway.user}") String gatewayUser,
            @Value("${vvkalinin.gateway.user.host}") String gatewayUserHost,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.gatewayToken = gatewayToken;
        this.gatewayAuth = gatewayAuth;
        this.gatewayAuthHost = gatewayAuthHost;
        this.gatewayUser = gatewayUser;
        this.gatewayUserHost = gatewayUserHost;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(gatewayAuth, r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.setRequestHeader("X-Forwarded-For", gatewayAuth)
                                .setRequestHeader("X-Forwarded-Token", gatewayToken))
                        .uri(gatewayAuthHost))
                .route(gatewayUser, r -> r.path("/api/v1/users/**")
                        .filters(f -> f.setRequestHeader("X-Forwarded-For", gatewayUser)
                                .setRequestHeader("X-Forwarded-Token", gatewayToken)
                                .filter(jwtAuthenticationFilter))
                        .uri(gatewayUserHost))

                .route(gatewayAuth, r -> r.path("/api/auth/swagger-ui/**", "/api/auth/v3/api-docs*/**")
                        .filters(f -> f.setRequestHeader("X-Forwarded-For", gatewayAuth)
                                .setRequestHeader("X-Forwarded-Token", gatewayToken))
                        .uri(gatewayAuthHost))
                .route(gatewayUser, r -> r.path("/api/users/swagger-ui/**", "/api/users/v3/api-docs*/**")
                        .filters(f -> f.setRequestHeader("X-Forwarded-For", gatewayUser)
                                .setRequestHeader("X-Forwarded-Token", gatewayToken))
                        .uri(gatewayUserHost))
                .build();
    }

}

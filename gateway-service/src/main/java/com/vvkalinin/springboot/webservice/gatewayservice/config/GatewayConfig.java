package com.vvkalinin.springboot.webservice.gatewayservice.config;

import com.vvkalinin.springboot.webservice.gatewayservice.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${vvkalinin.gateway.token}")
    private String gatewayToken;

    @Value("${vvkalinin.gateway.auth}")
    private String gatewayAuth;

    @Value("${vvkalinin.gateway.auth.host}")
    private String gatewayAuthHost;

    @Value("${vvkalinin.gateway.user}")
    private String gatewayUser;

    @Value("${vvkalinin.gateway.user.host}")
    private String gatewayUserHost;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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

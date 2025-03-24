package com.vvkalinin.springboot.webservice.gatewayservice.config;

import static org.mockserver.model.HttpRequest.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.server.ServerWebExchange;

import com.vvkalinin.springboot.webservice.gatewayservice.jwt.JwtAuthenticationFilter;
import com.vvkalinin.springboot.webservice.gatewayservice.jwt.JwtService;
import com.vvkalinin.springboot.webservice.gatewayservice.service.TokenBlacklistService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "vvkalinin.gateway.token=gateway-token",
        "vvkalinin.gateway.auth=auth-service",
        "vvkalinin.gateway.user=user-service",
        "spring.data.redis.host=redis",
        "spring.data.redis.port=0"
})
class GatewayConfigTest {

    static ClientAndServer authService;
    static ClientAndServer userService;

    static {
        authService = ClientAndServer.startClientAndServer(0);
        userService = ClientAndServer.startClientAndServer(0);
    }

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("vvkalinin.gateway.auth.host", () -> "http://localhost:" + authService.getLocalPort());
        registry.add("vvkalinin.gateway.user.host", () -> "http://localhost:" + userService.getLocalPort());
    }

    @AfterAll
    static void afterAll() {
        authService.stop();
        userService.stop();
    }

    @BeforeEach
    void setupMock() {
        Mockito.when(jwtAuthenticationFilter.filter(Mockito.any(), Mockito.any()))
                .thenAnswer(invocation -> {
                    ServerWebExchange exchange = invocation.getArgument(0);
                    GatewayFilterChain chain = invocation.getArgument(1);
                    return chain.filter(exchange);
                });
    }

    @Test
    void testRoutingToAuthServiceWithForwardedHeaders() {
        authService.when(request().withMethod("POST")
                        .withPath("/api/v1/auth/token")
                        .withHeader("X-Forwarded-For", ".*auth-service.*")
                        .withHeader("X-Forwarded-Token", "gateway-token"))
                .respond(HttpResponse.response("dummy response"));

        webTestClient.post().uri("/api/v1/auth/token")
                .exchange()
                .expectStatus().isOk();

        authService.verify(
                request()
                        .withPath("/api/v1/auth/token")
                        .withHeader("X-Forwarded-For", ".*auth-service.*")
                        .withHeader("X-Forwarded-Token", "gateway-token"),
                VerificationTimes.exactly(1));
    }

    @Test
    void testRoutingToUserServiceWithForwardedHeaders() {
        userService.when(request().withMethod("POST")
                        .withPath("/api/v1/users/me")
                        .withHeader(HttpHeaders.AUTHORIZATION, "Bearer dummy-token")
                        .withHeader("X-Forwarded-For", ".*user-service.*")
                        .withHeader("X-Forwarded-Token", "gateway-token"))
                .respond(HttpResponse.response("dummy response"));

        webTestClient.post().uri("/api/v1/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer dummy-token")
                .exchange()
                .expectStatus().isOk();

        userService.verify(
                request()
                        .withPath("/api/v1/users/me")
                        .withHeader("X-Forwarded-For", ".*user-service.*")
                        .withHeader("X-Forwarded-Token", "gateway-token"),
                VerificationTimes.exactly(1));
    }

    @Test
    void testRoutingToAuthSwaggerServiceWithForwardedHeaders() {
        authService.when(request().withMethod("POST")
                        .withPath("/api/auth/swagger-ui/swagger")
                        .withHeader("X-Forwarded-For", ".*auth-service.*")
                        .withHeader("X-Forwarded-Token", "gateway-token"))
                .respond(HttpResponse.response("dummy response"));

        webTestClient.post().uri("/api/auth/swagger-ui/swagger")
                .exchange()
                .expectStatus().isOk();

        authService.verify(
                request()
                        .withPath("/api/auth/swagger-ui/swagger")
                        .withHeader("X-Forwarded-For", ".*auth-service.*")
                        .withHeader("X-Forwarded-Token", "gateway-token"),
                VerificationTimes.exactly(1));
    }

    @Test
    void testRoutingToUserSwaggerServiceWithForwardedHeaders() {
        userService.when(request().withMethod("POST")
                        .withPath("/api/users/swagger-ui/swagger")
                        .withHeader("X-Forwarded-For", ".*user-service.*")
                        .withHeader("X-Forwarded-Token", "gateway-token"))
                .respond(HttpResponse.response("dummy response"));

        webTestClient.post().uri("/api/users/swagger-ui/swagger")
                .exchange()
                .expectStatus().isOk();

        userService.verify(
                request()
                        .withPath("/api/users/swagger-ui/swagger")
                        .withHeader("X-Forwarded-For", ".*user-service.*")
                        .withHeader("X-Forwarded-Token", "gateway-token"),
                VerificationTimes.exactly(1));
    }

}
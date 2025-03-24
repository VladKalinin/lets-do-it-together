package com.vvkalinin.springboot.webservice.gatewayservice.jwt;

import static com.vvkalinin.springboot.webservice.gatewayservice.enums.Token.REFRESH;
import static org.junit.jupiter.api.Assertions.*;
import static com.vvkalinin.springboot.webservice.gatewayservice.enums.Token.ACCESS;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vvkalinin.springboot.webservice.gatewayservice.config.GatewayConfig;
import com.vvkalinin.springboot.webservice.gatewayservice.service.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;

@SpringBootTest(properties = {
        "vvkalinin.jwt.secret=testSecret",
        "vvkalinin.jwt.issuer=testIssuer",
        "spring.data.redis.host=redis",
        "spring.data.redis.port=0"
})
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private GatewayConfig gatewayConfig;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;

    @Test
    void testValidToken() {
        String token = JWT.create()
                .withIssuer("testIssuer")
                .withClaim("type", ACCESS.name())
                .sign(Algorithm.HMAC256("testSecret"));

        DecodedJWT decodedJWT = jwtService.validateToken(token);
        assertNotNull(decodedJWT);
        assertEquals("testIssuer", decodedJWT.getIssuer());
    }

    @Test
    void testInvalidIssuer() {
        String token = JWT.create()
                .withIssuer("invalidIssuer")
                .withClaim("type", ACCESS.name())
                .sign(Algorithm.HMAC256("testSecret"));

        assertThrows(JWTVerificationException.class, () -> {
            jwtService.validateToken(token);
        });
    }

    @Test
    void testInvalidClaimType() {
        String token = JWT.create()
                .withIssuer("testIssuer")
                .withClaim("type", REFRESH.name())
                .sign(Algorithm.HMAC256("testSecret"));

        assertThrows(JWTVerificationException.class, () -> {
            jwtService.validateToken(token);
        });
    }

    @Test
    void testMissingClaim() {
        String token = JWT.create()
                .withIssuer("testIssuer")
                .sign(Algorithm.HMAC256("testSecret"));

        assertThrows(JWTVerificationException.class, () -> {
            jwtService.validateToken(token);
        });
    }

    @Test
    void testExpiredToken() {
        String token = JWT.create()
                .withIssuer("testIssuer")
                .withClaim("type", ACCESS.name())
                .withExpiresAt(Instant.now())
                .sign(Algorithm.HMAC256("testSecret"));

        assertThrows(JWTVerificationException.class, () -> {
            jwtService.validateToken(token);
        });
    }

}
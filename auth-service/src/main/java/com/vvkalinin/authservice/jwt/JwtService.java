package com.vvkalinin.authservice.jwt;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static com.vvkalinin.authservice.enums.Token.ACCESS;
import static com.vvkalinin.authservice.enums.Token.REFRESH;
import static java.lang.System.currentTimeMillis;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vvkalinin.authservice.model.User;
import com.vvkalinin.authservice.service.redis.TokenBlackListService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final TokenBlackListService tokenBlackListService;

    @Value("${vvkalinin.jwt.secret}")
    private String jwtSecret;

    @Value("${vvkalinin.jwt.expiration}")
    private int jwtExpirationMs;

    @Value("${vvkalinin.jwt.refresh.expiration}")
    private int jwtRefreshExpirationMs;

    @Value("${spring.application.name}")
    private String issuer;

    public JwtService(TokenBlackListService tokenBlackListService) {
        this.tokenBlackListService = tokenBlackListService;
    }

    public String generateToken(User user) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(user.username())
                .withIssuer(issuer)
                .withClaim("email", user.email())
                .withClaim("type", ACCESS.name())
                .withIssuedAt(new Date(currentTimeMillis()))
                .withExpiresAt(new Date(currentTimeMillis() + jwtExpirationMs))
                .sign(HMAC256(jwtSecret));
    }

    public String generateRefreshToken(User user) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(user.username())
                .withIssuer(issuer)
                .withClaim("type", REFRESH.name())
                .withIssuedAt(new Date(currentTimeMillis()))
                .withExpiresAt(new Date(currentTimeMillis() + jwtRefreshExpirationMs))
                .sign(HMAC256(jwtSecret));
    }

    public String validateAccessToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(HMAC256(jwtSecret))
                .withIssuer(issuer)
                .withClaim("type", ACCESS.name())
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }

    public String validateRefreshToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(HMAC256(jwtSecret))
                .withIssuer(issuer)
                .withClaim("type", REFRESH.name())
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }

}

package com.vvkalinin.springboot.webservice.gatewayservice.jwt;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static com.vvkalinin.springboot.webservice.gatewayservice.enums.Token.ACCESS;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final String jwtSecret;

    private final String issuer;

    public JwtService(@Value("${vvkalinin.jwt.secret}") String jwtSecret,
                      @Value("${vvkalinin.jwt.issuer}") String issuer) {
        this.jwtSecret = jwtSecret;
        this.issuer = issuer;
    }

    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(HMAC256(jwtSecret))
                .withIssuer(issuer)
                .withClaim("type", ACCESS.name())
                .build();
        return verifier.verify(token);
    }

}

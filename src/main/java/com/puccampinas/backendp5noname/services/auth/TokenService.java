package com.puccampinas.backendp5noname.services.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.puccampinas.backendp5noname.domain.RefreshToken;
import com.puccampinas.backendp5noname.domain.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Log4j2
public class TokenService {
    static final String issuer = "MyApp";

    private long accessTokenExpirationMs;
    private long refreshTokenExpirationMs;

    private Algorithm accessTokenAlgorithm;
    private Algorithm refreshTokenAlgorithm;
    private JWTVerifier accessTokenVerifier;
    private JWTVerifier refreshTokenVerifier;

    public TokenService(@Value("${accessTokenSecret}") String accessTokenSecret,
                        @Value("${refreshTokenSecret}") String refreshTokenSecret,
                        @Value("${com.example.demo.refreshTokenExpirationDays}") int refreshTokenExpirationDays,
                        @Value("${com.example.demo.accessTokenExpirationMinutes}") int accessTokenExpirationMinutes) {
        accessTokenExpirationMs = (long) accessTokenExpirationMinutes * 60 * 1000;
        refreshTokenExpirationMs = (long) refreshTokenExpirationDays * 24 * 60 * 60 * 1000;
        accessTokenAlgorithm = Algorithm.HMAC512(accessTokenSecret);
        refreshTokenAlgorithm = Algorithm.HMAC512(refreshTokenSecret);
        accessTokenVerifier = JWT.require(accessTokenAlgorithm)
                .withIssuer(issuer)
                .build();
        refreshTokenVerifier = JWT.require(refreshTokenAlgorithm)
                .withIssuer(issuer)
                .build();
    }

    public String generateAccessToken(User user) {
        String token = JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getId().toString()) // Certifique-se de que o ID está sendo convertido para string
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + accessTokenExpirationMs))
                .sign(accessTokenAlgorithm);
        log.info("Generated Access Token: " + token);
        return token;
    }

    public String generateRefreshToken(User user, RefreshToken refreshToken) {
        String token = JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getId().toString()) // Certifique-se de que o ID está sendo convertido para string
                .withClaim("tokenId", refreshToken.getId())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + refreshTokenExpirationMs))
                .sign(refreshTokenAlgorithm);
        log.info("Generated Refresh Token: " + token);
        return token;
    }

    private Optional<DecodedJWT> decodeAccessToken(String token) {
        try {
            log.info("Decoding Access Token: " + token);
            return Optional.of(accessTokenVerifier.verify(token));
        } catch (JWTVerificationException e) {
            log.error("Invalid access token", e);
        }
        return Optional.empty();
    }

    private Optional<DecodedJWT> decodeRefreshToken(String token) {
        try {
            log.info("Decoding Refresh Token: " + token);
            return Optional.of(refreshTokenVerifier.verify(token));
        } catch (JWTVerificationException e) {
            log.error("Invalid refresh token", e);
        }
        return Optional.empty();
    }

    public boolean validateAccessToken(String token) {
        boolean isValid = decodeAccessToken(token).isPresent();
        log.info("Access Token is valid: " + isValid);
        return isValid;
    }

    public boolean validateRefreshToken(String token) {
        boolean isValid = decodeRefreshToken(token).isPresent();
        log.info("Refresh Token is valid: " + isValid);
        return isValid;
    }

    public String getUserIdFromAccessToken(String token) {
        Optional<DecodedJWT> decodedJWT = decodeAccessToken(token);
        if (decodedJWT.isPresent()) {
            String userId = decodedJWT.get().getSubject();
            log.info("Extracted userId from Access Token: " + userId);
            return userId;
        } else {
            log.warn("Failed to extract userId from Access Token");
            return null;
        }
    }

    public String getUserIdFromRefreshToken(String token) {
        Optional<DecodedJWT> decodedJWT = decodeRefreshToken(token);
        if (decodedJWT.isPresent()) {
            String userId = decodedJWT.get().getSubject();
            log.info("Extracted userId from Refresh Token: " + userId);
            return userId;
        } else {
            log.warn("Failed to extract userId from Refresh Token");
            return null;
        }
    }

    public String getTokenIdFromRefreshToken(String token) {
        Optional<DecodedJWT> decodedJWT = decodeRefreshToken(token);
        if (decodedJWT.isPresent()) {
            String tokenId = decodedJWT.get().getClaim("tokenId").asString();
            log.info("Extracted tokenId from Refresh Token: " + tokenId);
            return tokenId;
        } else {
            log.warn("Failed to extract tokenId from Refresh Token");
            return null;
        }
    }
}
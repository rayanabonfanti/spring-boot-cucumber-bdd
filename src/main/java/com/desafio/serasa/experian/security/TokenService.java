package com.desafio.serasa.experian.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.desafio.serasa.experian.domain.pessoa.Pessoa;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    private final StringRedisTemplate redisTemplate;
    @Value("${api.security.token.secret}")
    private String secret;

    public TokenService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateToken(Pessoa pessoa) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(pessoa.getLogin())
                    .withExpiresAt(genExpiration())
                    .sign(algorithm);

            saveUserInRedis(pessoa, 10L * 60);

            return token;
        } catch (JWTCreationException exception) {
            throw new IllegalArgumentException("Error while generating token", exception);
        }
    }

    public Pessoa validateTokenAndRetrieveUser(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String subject = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

            if (subject != null) {
                String userJson = getUserFromRedis(subject);
                if (userJson != null) {
                    return convertJsonToUser(userJson);
                }
            }
            return null;
        } catch (JWTVerificationException exception) {
            throw new IllegalArgumentException("Error while validation token", exception);
        }
    }

    public Instant genExpiration() {
        return Instant.now().plus(600, ChronoUnit.SECONDS);
    }

    private String getUserFromRedis(String subject) {
        return redisTemplate.opsForValue().get(subject);
    }

    private void saveUserInRedis(Pessoa pessoa, long expirationSeconds) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String userJson = convertUserToJson(pessoa);
        ops.set(pessoa.getLogin(), userJson, expirationSeconds, TimeUnit.SECONDS);
    }

    private Pessoa convertJsonToUser(String userJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(userJson, Pessoa.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON to User", e);
        }
    }

    private String convertUserToJson(Pessoa pessoa) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(pessoa);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting User to JSON", e);
        }
    }

}

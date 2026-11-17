package com.klu.springmvc.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    // Secret key used to generate and validate JWT
    private String key = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiMSIsInVuIjoiaGFyaWthIiwiaWQiOiIxIiwiaWF0IjoxNzg3MjA1MjQ4LCJleHAiOjE3ODcyOTE2NDh9.0CNaS2MvxfmzvYRJHaBTW1Lp_yNH3xx9czjTpXYV-oBqQnBm4rypDj6qeGZKj6i43-oW39Zn1y8VrI6MIMrdyQ";

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    // Generate JWT Token
    public String generateJWT(Map<String, String> u1, String role, Integer id) {

        Map<String, Object> claim = new HashMap<>();

        claim.put("un", u1.get("username"));
        claim.put("role", role);
        claim.put("id", id.toString());

        return Jwts.builder()
                .claims(claim)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 86400000))
                .signWith(getKey())
                .compact();
    }

    // Validate JWT Token
    public boolean validateToken(String token) {

        try {

            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    // Get username from JWT
    public String getUsername(String token) {

        try {

            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("un", String.class);

        } catch (Exception e) {

            return null;
        }
    }
}
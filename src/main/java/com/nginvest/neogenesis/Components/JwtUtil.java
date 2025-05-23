package com.nginvest.neogenesis.Components;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.nginvest.neogenesis.Model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String password = "secret_key_honey_big_pie_Whith_cream_cheese";

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(password.getBytes());
    }

    public String generateToken(User user){
        return Jwts.builder()
            .setSubject(String.valueOf(user.getId()))
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public Long validateTokenAndGetUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
}
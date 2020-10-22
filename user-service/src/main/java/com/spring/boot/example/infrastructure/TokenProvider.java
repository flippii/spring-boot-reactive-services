package com.spring.boot.example.infrastructure;

import com.spring.boot.example.configuration.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Date;

import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

    private Key key;
    private final JwtProperties jwt;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwt.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expireTimeFromNow())
                .compact();
    }

    private Date expireTimeFromNow() {
        return new Date(System.currentTimeMillis() + jwt.getSessionTime() * 1000);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        User principal = new User(claims.getSubject(), "", emptyList());

        return new UsernamePasswordAuthenticationToken(principal, token, emptyList());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);

            return true;
        } catch (Exception e) {
            log.error("invalid jwt token", e);
            return false;
        }
    }

}

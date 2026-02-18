package com.zizonhyunwoo.board.util;


import com.zizonhyunwoo.board.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.key}")
    private String jwtKey;

    private Key key; // 처음엔 비워두고 ✨
    private final long expirationTime = 3600000;

    @jakarta.annotation.PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtKey.getBytes());
    }

    public String createToken(UserEntity user) {
        Date now = new Date();
        Claims claims = Jwts.claims()
                .add("email", user.getEmail())
                .add("nickname", user.getNickname())
                .add("role", user.getRole())
                .add("userId", user.getId().toString())
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationTime))
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload(); // 전체 클레임 보석함을 가져와요 ✨
    }

    public String getNickname(String token) {
        return getClaims(token).get("nickname", String.class);
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }



    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

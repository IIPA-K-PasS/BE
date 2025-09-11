package com.k_passs.backend.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    // JWT 서명을 위한 키
    private final Key key;

    // JWT 유효 시간: 24시간 (Access Token)
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24시간

    // Refresh Token 유효 시간: 7일
    private static final long REFRESH_EXPIRATION_MS = 1000L * 60 * 60 * 24 * 7; // 7 days

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 환경 변수로부터 읽어온 secretKey 로그 출력
        log.info("Loaded JWT secret key: {}", secretKey);
    }

    // Access Token 생성 메서드
    public String createAccessToken(Long userId) {
        log.info("Access Token 생성 중! userId: {}", userId);
        log.info("Access Token 생성에 사용된 secret key: {}", Base64.getEncoder().encodeToString(key.getEncoded()));
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 생성 메서드
    public String createRefreshToken(Long userId) {
        log.info("Refresh Token 생성 중! userId: {}", userId);
        log.info("Refresh Token 생성에 사용된 secret key: {}", Base64.getEncoder().encodeToString(key.getEncoded()));
        Date now = new Date();
        Date expiry = new Date(now.getTime() + REFRESH_EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    // JWT 토큰에서 사용자 ID 추출
    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }

    // JWT 유효성 검증 메서드
    public boolean validateToken(String token) {
        try {
            log.info("JWT 검증에 사용된 secret key: {}", Base64.getEncoder().encodeToString(key.getEncoded()));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }
}
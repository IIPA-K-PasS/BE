package com.k_passs.backend.domain.user.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    // JWT 서명을 위한 비밀 키 생성
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 토큰 만료 시간 설정 (1시간)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    // 주어진 사용자 ID를 기반으로 JWT 토큰 생성
    public static String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // 전달받은 JWT 토큰을 검증하고 사용자 ID(subject)를 추출
    public static String validateAndGetUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
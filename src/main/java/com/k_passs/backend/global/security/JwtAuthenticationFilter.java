package com.k_passs.backend.global.security;

import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.repogitory.UserRepository;
import com.k_passs.backend.global.jwt.JwtProvider;
import com.k_passs.backend.global.oauth2.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("🔐 JwtAuthenticationFilter 작동 시작");

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("🪪 받은 토큰: " + token);

            if (jwtProvider.validateToken(token)) {
                Long userId = jwtProvider.getUserId(token);
                System.out.println("✅ 토큰 유효, userId: " + userId);

                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    CustomOAuth2User principal = new CustomOAuth2User(user);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("🔐 SecurityContext에 인증 저장 완료");
                } else {
                    System.out.println("❌ 유저 없음");
                }
            } else {
                System.out.println("❌ 유효하지 않은 JWT");
            }
        } else {
            System.out.println("❌ Authorization 헤더 없음 또는 형식 오류");
        }

        filterChain.doFilter(request, response);
    }
}
package com.k_passs.backend.domain.user.controller;


import com.k_passs.backend.domain.user.util.JwtUtil;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class KakaoAuthController {

    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");

        // 1. 카카오 API 호출로 토큰 검증
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://kapi.kakao.com/v2/user/me";
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "카카오 인증 실패"));
        }

        Map<String, Object> kakaoUser = response.getBody();
        String kakaoId = String.valueOf(kakaoUser.get("id"));

        // 실제로는 UserRepository에서 findByKakaoId() 같은 로직 필요
        boolean isNewUser = false; // 새 사용자라면 회원가입 처리

        // 3. JWT 발급
        String jwt = JwtUtil.generateToken(kakaoId);

        // 4. JSON 응답 반환
        return ResponseEntity.ok(Map.of(
                "jwt", jwt,
                "isNewUser", isNewUser,
                "kakaoId", kakaoId
        ));
    }
}
package com.k_passs.backend.domain.user.controller;


import com.k_passs.backend.domain.user.util.JwtUtil;
import com.k_passs.backend.global.error.code.status.ErrorStatus;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class KakaoAuthController {

    // 클라이언트에서 카카오 액세스 토큰을 전달받아 로그인 처리
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body) {
        // 클라이언트로부터 전달받은 액세스 토큰 추출
        String accessToken = body.get("accessToken");

        // 1. 카카오 API 호출로 토큰 유효성 검증 및 사용자 정보 조회
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken); // Bearer 토큰 형식으로 헤더 설정
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 카카오 사용자 정보 API
        String url = "https://kapi.kakao.com/v2/user/me";
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        // 카카오 인증 실패 시 401 Unauthorized 반환
        if (response.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity
                    .status(ErrorStatus._KAKAO_UNAUTHORIZED.getHttpStatus())
                    .body(ErrorStatus._KAKAO_UNAUTHORIZED.getReasonHttpStatus());
        }

        // 카카오 사용자 정보에서 ID 추출
        Map<String, Object> kakaoUser = response.getBody();
        String kakaoId = String.valueOf(kakaoUser.get("id"));

        // TODO: 실제 서비스에서는 UserRepository 등을 이용한 사용자 조회 및 회원가입 로직 필요
        boolean isNewUser = false; // 기본값으로 false 설정 (임시)

        // 3. JWT 발급 (사용자 식별용)
        String jwt = JwtUtil.generateToken(kakaoId);

        // 4. 클라이언트에 JWT, 사용자 신규 여부, 카카오 ID를 포함한 JSON 응답 반환
        return ResponseEntity.ok(Map.of(
                "jwt", jwt,
                "isNewUser", isNewUser,
                "kakaoId", kakaoId
        ));
    }
}
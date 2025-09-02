package com.k_passs.backend.domain.user.controller;

import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.global.error.code.status.ErrorStatus;
import com.k_passs.backend.global.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    // 현재 로그인한 사용자 정보를 조회하는 API
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal CustomOAuth2User user,
                                       @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // 요청 헤더에 포함된 Authorization 값을 콘솔에 출력 (디버깅 용도)
        System.out.println("Authorization 헤더 값: " + authHeader);

        // 인증된 사용자가 없는 경우 UNAUTHORIZED 상태 코드 반환
        if (user == null) {
            return ResponseEntity
                    .status(ErrorStatus._USER_UNAUTHORIZED.getHttpStatus())
                    .body(ErrorStatus._USER_UNAUTHORIZED.getReasonHttpStatus());
        }

        // 인증된 사용자의 정보를 반환
        return ResponseEntity.ok(user.getUser());
    }
}

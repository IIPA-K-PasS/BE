package com.k_passs.backend.domain.user.controller;

import com.k_passs.backend.domain.user.entity.User;
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

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal CustomOAuth2User user,
                                       @RequestHeader(value = "Authorization", required = false) String authHeader) {

        System.out.println("📥 Authorization 헤더 값: " + authHeader);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유저 인증 실패");
        }

        return ResponseEntity.ok(user.getUser());
    }
}

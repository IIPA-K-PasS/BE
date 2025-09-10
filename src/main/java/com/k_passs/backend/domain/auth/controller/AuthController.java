package com.k_passs.backend.domain.auth.controller;

import com.k_passs.backend.domain.auth.dto.KakaoLoginRequest;
import com.k_passs.backend.domain.auth.dto.TokenResponse;
import com.k_passs.backend.domain.auth.dto.UserInfoResponse;
import com.k_passs.backend.domain.auth.service.AuthService;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.global.jwt.JwtProvider;
import com.k_passs.backend.global.oauth2.KakaoIdTokenValidator;
import com.nimbusds.jwt.SignedJWT;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 카카오 로그인
    @Operation(
            summary = "카카오 로그인",
            description = "카카오에서 발급받은 ID 토큰을 검증하고, 자체 AccessToken/RefreshToken을 발급합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공, 토큰 반환"),
            @ApiResponse(responseCode = "401", description = "ID 토큰이 유효하지 않음")
    })
    @PostMapping("/kakao")
    public ResponseEntity<TokenResponse> kakaoLogin(@RequestBody KakaoLoginRequest request) {
        TokenResponse tokenResponse = authService.kakaoLogin(request.getIdToken());
        return ResponseEntity.ok(tokenResponse);
    }

    // 유저 정보 요청
    @Operation(
            summary = "유저 정보 요청",
            description = "JWT AccessToken으로 현재 로그인된 유저 정보를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 반환 성공"),
            @ApiResponse(responseCode = "401", description = "JWT가 없거나 유효하지 않음")
    })
    @GetMapping("/user-info")
    public ResponseEntity<UserInfoResponse.AuthUserInfo> getUserInfo(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(authService.getUserInfo(user));
    }
}

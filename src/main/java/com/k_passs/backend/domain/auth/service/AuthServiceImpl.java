package com.k_passs.backend.domain.auth.service;

import java.util.Map;

import com.k_passs.backend.domain.auth.converter.AuthConverter;
import com.k_passs.backend.domain.auth.dto.TokenResponse;
import com.k_passs.backend.domain.auth.dto.UserInfoResponse;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.repogitory.UserRepository;
import com.k_passs.backend.global.jwt.JwtProvider;
import com.k_passs.backend.global.oauth2.KakaoIdTokenValidator;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final KakaoIdTokenValidator kakaoIdTokenValidator;

    @Override
    public TokenResponse kakaoLogin(String idToken) {
        try {
            if (!kakaoIdTokenValidator.validate(idToken)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid idToken");
            }

            SignedJWT signedJWT = SignedJWT.parse(idToken);
            var claims = signedJWT.getJWTClaimsSet();
            String kakaoUserId = claims.getSubject();

            String email = null;
            String nickname = null;
            try { email = claims.getStringClaim("email"); } catch (Exception ignored) {}
            try { nickname = claims.getStringClaim("nickname"); } catch (Exception ignored) {}

            // 카카오 idToken은 여러 구조로 정보가 들어올 수 있으므로, 여러 경로로 이메일/닉네임 추출 시도
            Object kakaoAccountObj = claims.getClaim("kakao_account");
            if (email == null && kakaoAccountObj instanceof Map<?,?> kakaoAcc) {
                Object emailObj = kakaoAcc.get("email");
                if (emailObj instanceof String s) email = s;
                Object profileObj = kakaoAcc.get("profile");
                if (nickname == null && profileObj instanceof Map<?,?> prof) {
                    Object nn = prof.get("nickname");
                    if (nn instanceof String s) nickname = s;
                }
            }

            if (nickname == null) {
                try { nickname = claims.getStringClaim("name"); } catch (Exception ignored) {}
                Object propsObj = claims.getClaim("properties");
                if (nickname == null && propsObj instanceof Map<?,?> props) {
                    Object nn = props.get("nickname");
                    if (nn instanceof String s) nickname = s;
                }
            }
            if (nickname == null) nickname = "새 유저";

            final String resolvedEmail = email;
            final String resolvedNickname = nickname;
            User user;

            // DB에서 사용자 조회 및 저장 로직
            // 이메일이 있으면 이메일로, 없으면 카카오 고유 ID로 사용자 조회/저장
            if (resolvedEmail != null && !resolvedEmail.isBlank()) {
                var existing = userRepository.findByEmail(resolvedEmail);
                if (existing.isPresent()) {
                    User u = existing.get();

                    // 기존 유저의 닉네임이 비어있으면, 카카오에서 받은 닉네임으로 업데이트
                    if ((u.getNickname() == null || u.getNickname().isBlank()) && resolvedNickname != null) {
                        u = User.builder()
                                .id(u.getId())
                                .email(u.getEmail())
                                .nickname(resolvedNickname)
                                .profileImageUrl(u.getProfileImageUrl())
                                .point(u.getPoint())
                                .build();
                        u = userRepository.save(u); // 닉네임 업데이트 후 저장
                    }
                    user = u;
                } else {
                    // 신규 유저 - 이메일과 닉네임으로 회원 가입
                    user = userRepository.save(
                            User.builder()
                                    .email(resolvedEmail)
                                    .nickname(resolvedNickname)
                                    .build()
                    );
                }
            } else {
                // 이메일이 없는 경우, 카카오 고유 ID로 임시 이메일 대체하여 회원 식별
                var existing = userRepository.findByEmail(kakaoUserId);
                if (existing.isPresent()) {
                    user = existing.get();
                } else {
                    user = userRepository.save(
                            User.builder()
                                    .email(kakaoUserId) // 임시로 카카오 ID를 이메일 필드에 저장
                                    .nickname(resolvedNickname)
                                    .build()
                    );
                }
            }

            // JWT 토큰(Access/Refresh) 발급
            String accessToken = jwtProvider.createAccessToken(user.getId());
            String refreshToken = jwtProvider.createRefreshToken(user.getId());

            // 최종적으로 토큰 응답 반환
            return new TokenResponse(accessToken, refreshToken);

        } catch (Exception e) {
            // idToken 파싱이나 처리 중 예외 발생 시 UNAUTHORIZED 반환
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "idToken parsing failed", e);
        }
    }

    @Override
    public UserInfoResponse.AuthUserInfo getUserInfo(User user) {
        // 사용자 정보 조회
        return AuthConverter.toAuthUserInfo(user);
    }
}
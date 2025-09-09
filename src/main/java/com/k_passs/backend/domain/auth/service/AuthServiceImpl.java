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

            // Fallbacks for Kakao-specific nested claim shapes
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

            // DB 조회 또는 신규 생성 (by email if available, else fallback)
            final String resolvedEmail = email;
            final String resolvedNickname = nickname;
            User user;

            if (resolvedEmail != null && !resolvedEmail.isBlank()) {
                var existing = userRepository.findByEmail(resolvedEmail);
                if (existing.isPresent()) {
                    User u = existing.get();
                    // 업데이트: 닉네임이나 프로필이 비어 있을 때 보강
                    if ((u.getNickname() == null || u.getNickname().isBlank()) && resolvedNickname != null) {
                        // 새 인스턴스를 만들었다면 반드시 저장하여 반영
                        u = User.builder()
                                .id(u.getId())
                                .email(u.getEmail())
                                .nickname(resolvedNickname)
                                .profileImageUrl(u.getProfileImageUrl())
                                .point(u.getPoint())
                                .build();
                        u = userRepository.save(u);
                    }
                    user = u;
                } else {
                    user = userRepository.save(
                            User.builder()
                                    .email(resolvedEmail)
                                    .nickname(resolvedNickname)
                                    .build()
                    );
                }
            } else {
                // TODO: 향후 providerUserId(=kakao sub) 컬럼을 도입해 이 분기 제거
                var existing = userRepository.findByEmail(kakaoUserId);
                if (existing.isPresent()) {
                    user = existing.get();
                } else {
                    user = userRepository.save(
                            User.builder()
                                    .email(kakaoUserId) // 임시 보관
                                    .nickname(resolvedNickname)
                                    .build()
                    );
                }
            }

            // 토큰 발급
            String accessToken = jwtProvider.createAccessToken(user.getId());
            String refreshToken = jwtProvider.createRefreshToken(user.getId());

            return new TokenResponse(accessToken, refreshToken);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "idToken parsing failed", e);
        }
    }

    @Override
    public UserInfoResponse.AuthUserInfo getUserInfo(User user) {
        return AuthConverter.toAuthUserInfo(user);
    }
}
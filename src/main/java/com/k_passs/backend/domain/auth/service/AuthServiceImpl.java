package com.k_passs.backend.domain.auth.service;

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
            String kakaoUserId = signedJWT.getJWTClaimsSet().getSubject();

            // DB 조회 또는 신규 생성
            User user = userRepository.findByEmail(kakaoUserId)
                    .orElseGet(() -> userRepository.save(
                            User.builder()
                                    .email(kakaoUserId)
                                    .nickname("새 유저")
                                    .build()
                    ));

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

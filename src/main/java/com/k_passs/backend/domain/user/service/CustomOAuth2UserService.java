package com.k_passs.backend.domain.user.service;

import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.repogitory.UserRepository;
import com.k_passs.backend.global.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // OAuth2 로그인 후 사용자 정보를 불러오는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        // 기본 구현을 통해 OAuth2 사용자 정보 조회
        OAuth2User oAuth2User = super.loadUser(request);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 카카오 계정 정보 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        // 이메일, 닉네임, 프로필 이미지 추출
        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");

        // 이메일로 기존 사용자 조회, 없으면 새로 저장
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .nickname(nickname)
                        .profileImageUrl(profileImageUrl)
                        .build()));

        // 사용자 정보를 기반으로 CustomOAuth2User 반환
        return new CustomOAuth2User(user);
    }
}
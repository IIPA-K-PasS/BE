package com.k_passs.backend.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UserInfoResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthUserInfo {
        private Long id;
        private String email;
        private String nickname;
        private String profileImageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoUserInfo {
        private String email;
        private String nickname;
        private String profileImageUrl;
    }
}



package com.k_passs.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetUserInfo {
        private Long id;
        private String nickname;
        private String email;
        private String profileImageUrl;
        private Long point;
    }
}

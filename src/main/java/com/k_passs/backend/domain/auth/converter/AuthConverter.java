package com.k_passs.backend.domain.auth.converter;

import com.k_passs.backend.domain.auth.dto.UserInfoResponse;
import com.k_passs.backend.domain.user.entity.User;

public class AuthConverter {

    public static UserInfoResponse.AuthUserInfo toAuthUserInfo(User user) {
        return UserInfoResponse.AuthUserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}

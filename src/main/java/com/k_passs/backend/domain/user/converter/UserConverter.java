package com.k_passs.backend.domain.user.converter;

import com.k_passs.backend.domain.user.dto.UserResponseDTO;
import com.k_passs.backend.domain.user.entity.User;
import org.springframework.stereotype.Component;

public class UserConverter {

    public static UserResponseDTO.GetUserInfo toGetUserInfo(User user) {
        return UserResponseDTO.GetUserInfo.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .point(user.getPoint())
                .build();
    }
}

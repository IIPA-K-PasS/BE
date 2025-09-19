package com.k_passs.backend.domain.user.service;

import com.k_passs.backend.domain.user.dto.UserResponseDTO;
import com.k_passs.backend.domain.user.entity.User;

public interface UserService {
    // 현재 로그인된 유저 정보 조회
    UserResponseDTO.GetUserInfo getUserInfo(User user);
}

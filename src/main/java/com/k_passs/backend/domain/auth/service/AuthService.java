package com.k_passs.backend.domain.auth.service;

import com.k_passs.backend.domain.auth.dto.UserInfoResponse;
import com.k_passs.backend.domain.user.entity.User;

public interface AuthService {
    UserInfoResponse.AuthUserInfo getUserInfo(User user);
}

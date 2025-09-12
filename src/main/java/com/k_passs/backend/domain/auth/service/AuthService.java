package com.k_passs.backend.domain.auth.service;

import com.k_passs.backend.domain.auth.dto.TokenResponse;
import com.k_passs.backend.domain.auth.dto.UserInfoResponse;
import com.k_passs.backend.domain.user.entity.User;

public interface AuthService {
    TokenResponse kakaoLogin(String idToken);
    UserInfoResponse.AuthUserInfo getUserInfo(User user);
}

package com.k_passs.backend.domain.auth.service;

import com.k_passs.backend.domain.auth.converter.AuthConverter;
import com.k_passs.backend.domain.auth.dto.UserInfoResponse;
import com.k_passs.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Override
    public UserInfoResponse.AuthUserInfo getUserInfo(User user) {
        return AuthConverter.toAuthUserInfo(user);
    }
}

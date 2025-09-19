package com.k_passs.backend.domain.user.service;

import com.k_passs.backend.domain.user.converter.UserConverter;
import com.k_passs.backend.domain.user.dto.UserRequestDTO;
import com.k_passs.backend.domain.user.dto.UserResponseDTO;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO.GetUserInfo getUserInfo(User user) {
        return UserConverter.toGetUserInfo(user);
    }

    @Override
    @Transactional
    public UserResponseDTO.UpdateNicknameResult updateNickname(User user, UserRequestDTO.UpdateNickname request) {
        user.setNickname(request.getNickname());
        User updated = userRepository.save(user);
        return UserConverter.toUpdateNicknameResult(updated);
    }
}

package com.k_passs.backend.domain.user.service;

import com.k_passs.backend.domain.user.dto.UserResponseDTO;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserService {
    // 현재 로그인된 유저 정보 조회
    UserResponseDTO.GetUserInfo getUserInfo(User user);

    // 특정 유저 ID로 조회
//    UserResponseDTO.GetUserInfo getUserInfoById(Long userId);
}


//@Service
//public class UserService {
//
//    private final UserRepository userRepository;
//
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public User getUserById(Long id) {
//        Optional<User> user = userRepository.findById(id);
//
//        if (user.isPresent()) {
//            System.out.println("[DEBUG] 사용자 조회 성공: " + user.get());
//        } else {
//            System.out.println("[DEBUG] 사용자 조회 실패: ID = " + id);
//        }
//
//        return user.orElseThrow(() -> new IllegalArgumentException("테스트용 사용자 ID를 찾을 수 없습니다: " + id));
//    }
//}

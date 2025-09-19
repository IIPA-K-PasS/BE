package com.k_passs.backend.domain.user.controller;

import com.k_passs.backend.domain.user.dto.UserResponseDTO;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.service.UserService;
import com.k_passs.backend.global.annotation.AuthUser;
import com.k_passs.backend.global.common.response.BaseResponse;
import com.k_passs.backend.global.error.code.status.ErrorStatus;
import com.k_passs.backend.global.error.code.status.SuccessStatus;
import com.k_passs.backend.global.oauth2.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    // 현재 로그인된 유저 정보 조회
    @GetMapping
    @Operation(summary = "현재 로그인된 유저 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "USER_200", description = "전체 꿀팁 정보를 성공적으로 조회했습니다.")
    })
    public BaseResponse<UserResponseDTO.GetUserInfo> getUserInfo(
//            @Parameter(hidden = true) @AuthUser User user
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        UserResponseDTO.GetUserInfo result= userService.getUserInfo(user);
        return BaseResponse.onSuccess(SuccessStatus.USER_GET_SUCCESS,result);
    }

//    // 특정 유저 정보 조회 (id 기준)
//    @GetMapping("/{userId}")
//    @Operation(summary = "전체 꿀팁 목록을 조회합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "TIP_200", description = "전체 꿀팁 정보를 성공적으로 조회했습니다.")
//    })
//    public ResponseEntity<UserResponseDTO.GetUserInfo> getUserInfo(@PathVariable Long userId) {
//        return ResponseEntity.ok(userService.getUserInfoById(userId));
//    }
}

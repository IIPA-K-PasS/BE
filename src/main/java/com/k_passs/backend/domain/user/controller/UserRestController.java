package com.k_passs.backend.domain.user.controller;

import com.k_passs.backend.domain.user.dto.UserResponseDTO;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.service.UserService;
import com.k_passs.backend.global.common.response.BaseResponse;
import com.k_passs.backend.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;

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
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        UserResponseDTO.GetUserInfo result= userService.getUserInfo(user);
        return BaseResponse.onSuccess(SuccessStatus.USER_GET_SUCCESS,result);
    }
}

package com.k_passs.backend.domain.tip.api;

import com.k_passs.backend.domain.tip.dto.TipResponseDTO;
import com.k_passs.backend.domain.tip.service.TipService;
import com.k_passs.backend.global.common.response.BaseResponse;
import com.k_passs.backend.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/tip")
public class TipRestController {
    private final TipService tipService;



    @GetMapping("/{tipId}")
    @Operation(summary = "특정 꿀팁의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "TIP_200", description = "특정 꿀팁의 정보를 성공적으로 조회했습니다.")
    })
    @Parameters({
            @Parameter(name = "tipId", description = "꿀팁의 id, path variable 입니다.")
    })
    public BaseResponse<TipResponseDTO.GetTipResult> getTip(
            @PathVariable Long tipId
    ) {
        TipResponseDTO.GetTipResult result = tipService.getTip(tipId);
        return BaseResponse.onSuccess(SuccessStatus.TIP_SUCCESS, result);
    }

    @GetMapping
    @Operation(summary = "전체 꿀팁 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "TIP_200", description = "전체 꿀팁 정보를 성공적으로 조회했습니다.")
    })
    public BaseResponse<List<TipResponseDTO.GetAllTipResult>> getAllTips() {
        List<TipResponseDTO.GetAllTipResult> result = tipService.getAllTips();
        return BaseResponse.onSuccess(SuccessStatus.TIP_SUCCESS, result);
    }
}

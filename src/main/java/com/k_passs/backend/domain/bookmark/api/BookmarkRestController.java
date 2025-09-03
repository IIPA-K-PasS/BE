package com.k_passs.backend.domain.bookmark.api;

import com.k_passs.backend.domain.bookmark.dto.BookmarkRequestDTO;
import com.k_passs.backend.domain.bookmark.dto.BookmarkResponseDTO;
import com.k_passs.backend.domain.bookmark.service.BookmarkService;
import com.k_passs.backend.global.common.response.BaseResponse;
import com.k_passs.backend.global.error.code.status.SuccessStatus;
import com.k_passs.backend.global.oauth2.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
@Validated
public class BookmarkRestController {

    private final BookmarkService bookMarkService;

    ///  북마크를 표시하면 보내야하니까?
    @PostMapping("")
    @Operation(summary = "특정 꿀팁에 북마크 요청 또는 취소하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "BOOKMARK_200", description = "북마크 상태가 성공적으로 변경되었습니다.")
    })
    public BaseResponse<BookmarkResponseDTO.BookmarkResult> bookmark(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestBody @Valid BookmarkRequestDTO.Bookmark request
    ) {
        BookmarkResponseDTO.BookmarkResult result =
                bookMarkService.updateBookmarkStatus(
                        user.getUser(),
                        request.getTipId(),
                        request.getIsBookmarked()
                );

        return BaseResponse.onSuccess(SuccessStatus.BOOKMARK_STATUS_CHANGED, result);
    }

}

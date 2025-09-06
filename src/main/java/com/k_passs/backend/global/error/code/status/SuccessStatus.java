package com.k_passs.backend.global.error.code.status;

import com.k_passs.backend.global.error.code.BaseCode;
import com.k_passs.backend.global.error.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    //Common
    OK(HttpStatus.OK, "COMMON_200", "성공입니다."),

    // Bookmark
    BOOKMARK_STATUS_CHANGED(HttpStatus.OK, "BOOKMARK_200","북마크 상태가 성공적으로 변경되었습니다."),

    // Tip
    TIP_SUCCESS(HttpStatus.OK,"TIP_200", "꿀팁이 성공적으로 조회되었습니다."),
    ;



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}

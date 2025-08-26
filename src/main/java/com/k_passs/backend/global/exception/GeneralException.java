package com.k_passs.backend.global.exception;

import com.k_passs.backend.global.error.code.BaseErrorCode;
import com.k_passs.backend.global.error.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}

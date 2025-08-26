package com.k_passs.backend.global.error.code;

public interface BaseErrorCode {
    String getCode();

    String getMessage();

    ErrorReasonDTO getReasonHttpStatus();
}

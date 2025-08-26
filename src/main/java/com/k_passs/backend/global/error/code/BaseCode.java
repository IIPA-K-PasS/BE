package com.k_passs.backend.global.error.code;

public interface BaseCode {
    String getCode();

    String getMessage();

    ReasonDTO getReasonHttpStatus();
}

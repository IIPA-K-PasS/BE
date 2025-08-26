package com.k_passs.backend.domain.model.exception;


import com.k_passs.backend.global.error.code.BaseErrorCode;
import com.k_passs.backend.global.exception.GeneralException;

public class TestException extends GeneralException {

    public TestException(BaseErrorCode code) {
        super(code);
    }
}

package com.k_passs.backend.domain.model.api;

import com.k_passs.backend.domain.model.exception.TestException;
import com.k_passs.backend.global.common.response.BaseResponse;
import com.k_passs.backend.global.error.code.status.ErrorStatus;
import com.k_passs.backend.global.error.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    @GetMapping("/execute")
    public BaseResponse<Void> test(@RequestParam String error){

        if(error.equals("yes")){
            throw new TestException(ErrorStatus._BAD_REQUEST);
        }

        return BaseResponse.onSuccess(SuccessStatus.OK,null);
    }
}

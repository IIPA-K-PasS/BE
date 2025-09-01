package com.k_passs.backend.domain.bill.dto;

import com.k_passs.backend.domain.model.enums.BillType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BillOcrResponseDto {
    //임시
    private BillType billType;
    private Integer amount;
    private String usagePeriod;
}

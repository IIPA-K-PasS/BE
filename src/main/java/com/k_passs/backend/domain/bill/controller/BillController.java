package com.k_passs.backend.domain.bill.controller;

import com.k_passs.backend.domain.bill.dto.BillOcrResponseDto;
import com.k_passs.backend.domain.bill.service.BillOcrService;
import com.k_passs.backend.domain.model.enums.BillType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BillController {

    private final BillOcrService billOcrService;

    @PostMapping(value = "/user/{userId}/bill/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BillOcrResponseDto> analyzeBill(
            @PathVariable Long userId,
            @RequestParam("billType") BillType billType,
            @RequestParam("billImage") MultipartFile billImage) {

        BillOcrResponseDto result = billOcrService.analyzeBill(userId, billType, billImage);
        return ResponseEntity.ok(result);
    }
}
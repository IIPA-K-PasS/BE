package com.k_passs.backend.domain.bill.service;

import com.k_passs.backend.domain.bill.dto.BillOcrResponseDto;
import com.k_passs.backend.domain.bill.entity.Bill;
import com.k_passs.backend.domain.bill.repository.BillRepository;
import com.k_passs.backend.domain.model.enums.BillStatus;
import com.k_passs.backend.domain.model.enums.BillType;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BillOcrService {

    private final BillRepository billRepository;
    private final UserRepository userRepository;

    @Transactional
    public BillOcrResponseDto analyzeBill(Long userId, BillType billType, MultipartFile billImage) {
        // 1. DB에 저장된 테스트용 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        // 2. [임시] OCR 분석 로직 (지금은 가짜 데이터 사용)
        System.out.println("전달받은 파일: " + billImage.getOriginalFilename());
        System.out.println("고지서 종류: " + billType);

        Integer fakeAmount = 12345; // 가짜 금액
        String fakePeriod = "2025-08"; // 가짜 사용기간

        // 3. 분석된 정보로 Bill 엔티티를 만들어 DB에 저장
        Bill newBill = Bill.builder()
                .user(user)
                .category(billType)
                .totalAmount(BigDecimal.valueOf(fakeAmount))
                .periodMonth(LocalDate.now()) // 임시로 현재 날짜 저장
                .status(BillStatus.UNPAID) // 임시로 '미납' 상태로 저장
                .build();
        billRepository.save(newBill);

        // 4. Controller에 반환할 응답 DTO 생성
        return BillOcrResponseDto.builder()
                .billType(billType)
                .amount(fakeAmount)
                .usagePeriod(fakePeriod)
                .build();
    }
}
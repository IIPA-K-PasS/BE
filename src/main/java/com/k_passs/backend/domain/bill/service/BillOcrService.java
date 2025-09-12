package com.k_passs.backend.domain.bill.service;

import com.k_passs.backend.domain.bill.dto.BillOcrResponseDto;
import com.k_passs.backend.domain.bill.entity.Bill;
import com.k_passs.backend.domain.bill.repository.BillRepository;
import com.k_passs.backend.domain.model.enums.BillStatus;
import com.k_passs.backend.domain.model.enums.BillType;
import com.k_passs.backend.domain.ocr.client.OcrApiClient; // 1. OcrApiClient 임포트
import com.k_passs.backend.domain.ocr.dto.NaverOcrResponseDto; // 1. NaverOcrResponseDto 임포트
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BillOcrService {

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final OcrApiClient ocrApiClient; // 2. OcrApiClient 의존성 주입

    @Transactional
    public BillOcrResponseDto analyzeBill(Long userId, BillType billType, MultipartFile billImage) {
        // 1. DB에 저장된 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        // 2. OcrApiClient를 통해 실제 네이버 OCR API 호출
        NaverOcrResponseDto ocrResult = ocrApiClient.requestOcr(billImage);

        // 3. OCR 결과에서 모든 텍스트를 하나로 합치기
        String rawText = extractTextFromOcrResult(ocrResult);
        System.out.println("### OCR 분석 전체 텍스트: " + rawText); // 콘솔에서 결과 확인

        // 4. 텍스트에서 금액과 사용기간 파싱
        Integer amount = parseAmountFromText(rawText, billType);
        String usagePeriod = parsePeriodFromText(rawText, billType); // "YYYY-MM" 형식

        // 5. 파싱된 정보로 Bill 엔티티를 만들어 DB에 저장
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth ym = YearMonth.parse(usagePeriod, formatter);
        LocalDate periodDate = ym.atDay(1);

        Bill newBill = Bill.builder()
                .user(user)
                .category(billType)
                .totalAmount(BigDecimal.valueOf(amount))
                .periodMonth(periodDate) // 파싱된 날짜로 저장
                .status(BillStatus.UNPAID)
                .build();
        billRepository.save(newBill);

        // 6. Controller에 반환할 응답 DTO 생성
        return BillOcrResponseDto.builder()
                .billType(billType)
                .amount(amount)
                .usagePeriod(usagePeriod)
                .rawText(rawText)
                .build();
    }

    /**
     * NaverOcrResponseDto에서 모든 텍스트를 추출하여 하나의 문자열로 합칩니다.
     */
    private String extractTextFromOcrResult(NaverOcrResponseDto ocrResult) {
        if (ocrResult == null || ocrResult.getImages() == null) {
            return "";
        }
        StringBuilder textBuilder = new StringBuilder();
        ocrResult.getImages().forEach(image ->
                image.getFields().forEach(field ->
                        textBuilder.append(field.getInferText()).append(" ")
                )
        );
        return textBuilder.toString();
    }

    /**
     * OCR 텍스트에서 금액을 추출합니다.
     */
    private Integer parseAmountFromText(String rawText, BillType billType) {
        // TODO: billType에 따라 다른 정규 표현식을 사용하여 금액을 파싱하는 로직 구현 필요
        // 예시: "청구금액 12,345원", "합계 54,321" 등의 패턴 찾기
        Pattern pattern = Pattern.compile("청구금액\\s*([\\d,]+)원?"); // "청구금액" 뒤 숫자 추출 예시
        Matcher matcher = pattern.matcher(rawText);
        if (matcher.find()) {
            String amountStr = matcher.group(1).replaceAll(",", ""); // 쉼표 제거
            return Integer.parseInt(amountStr);
        }
        return 0; // 금액을 찾지 못했을 경우 기본값
    }

    /**
     * OCR 텍스트에서 사용 기간(YYYY-MM)을 추출합니다.
     */
    private String parsePeriodFromText(String rawText, BillType billType) {
        // TODO: billType에 따라 다른 정규 표현식을 사용하여 기간을 파싱하는 로직 구현 필요
        // 예시: "2025년 08월분", "사용기간: 2025. 08. 01 ~ 2025. 08. 31" 등의 패턴 찾기
        Pattern pattern = Pattern.compile("(\\d{4})년\\s*(\\d{1,2})월"); // "YYYY년 MM월" 패턴 추출 예시
        Matcher matcher = pattern.matcher(rawText);
        if (matcher.find()) {
            String year = matcher.group(1);
            String month = String.format("%02d", Integer.parseInt(matcher.group(2))); // 월을 두 자리로 포맷팅
            return year + "-" + month;
        }
        return "2025-01"; // 기간을 찾지 못했을 경우 기본값
    }
}
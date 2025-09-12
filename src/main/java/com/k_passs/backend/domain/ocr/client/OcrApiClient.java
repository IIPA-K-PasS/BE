package com.k_passs.backend.domain.ocr.client;

import com.k_passs.backend.domain.ocr.dto.NaverOcrResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OcrApiClient {

    @Value("${ncp.api.url}")
    private String apiUrl;

    @Value("${ncp.api.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate;

    public NaverOcrResponseDto requestOcr(MultipartFile imageFile) {
        try {
            // 1. 요청 헤더 설정: 인증 키 포함
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-OCR-SECRET", secretKey);

            // 2. 요청 바디 설정: 네이버 OCR API 명세에 따름
            String base64Image = encodeImageToBase64(imageFile);
            String format = getFileExtension(imageFile);

            Map<String, Object> image = new HashMap<>();
            image.put("format", format);
            image.put("name", "bill-image");
            image.put("data", base64Image);

            Map<String, Object> body = new HashMap<>();
            body.put("version", "V2");
            body.put("requestId", UUID.randomUUID().toString());
            body.put("timestamp", System.currentTimeMillis());
            body.put("images", Collections.singletonList(image));

            // 3. HTTP 요청 엔티티 생성
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 4. RestTemplate을 사용하여 API 호출 및 응답 받기
            NaverOcrResponseDto response = restTemplate.postForObject(apiUrl, requestEntity, NaverOcrResponseDto.class);

            return response;

        } catch (IOException e) {
            throw new RuntimeException("이미지 파일을 처리하는 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("OCR API를 호출하는 중 오류가 '발생했습니다.", e);
        }
    }

    // 이미지를 Base64로 인코딩하는 헬퍼 메소드
    private String encodeImageToBase64(MultipartFile imageFile) throws IOException {
        return Base64.getEncoder().encodeToString(imageFile.getBytes());
    }

    // 파일 확장자를 추출하는 헬퍼 메소드
    private String getFileExtension(MultipartFile imageFile) {
        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.lastIndexOf('.') == -1) {
            return "jpg";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
    }
}
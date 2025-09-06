package com.k_passs.backend.domain.tip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TipResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetTipResult {
        private Long id;
        private String title;
        private String content;
        private String imageUrl;
        private String hashtags;
        private String infoText;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAllTipResult {
        private Long id;
        private String title;
        private String imageUrl;
        private String hashtags;
    }
}

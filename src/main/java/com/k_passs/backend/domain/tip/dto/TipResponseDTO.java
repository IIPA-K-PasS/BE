package com.k_passs.backend.domain.tip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
        private List<String> hashtags;
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
        private List<String> hashtags;
    }
}

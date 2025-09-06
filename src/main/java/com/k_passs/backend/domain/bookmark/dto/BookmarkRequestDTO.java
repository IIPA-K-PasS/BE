package com.k_passs.backend.domain.bookmark.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookmarkRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static  class Bookmark {
        @NotNull
        private Long tipId;

        @NotNull
        private Boolean isBookmarked;
    }
}

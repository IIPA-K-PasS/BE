package com.k_passs.backend.domain.tip.converter;

import com.k_passs.backend.domain.tip.entity.Tip;
import com.k_passs.backend.domain.tip.dto.TipResponseDTO;

public class TipConverter {

    public static TipResponseDTO.GetTipResult toGetTip(Tip tip) {
        return TipResponseDTO.GetTipResult.builder()
                .id(tip.getId())
                .title(tip.getTitle())
                .content(tip.getContent())
                .imageUrl(tip.getImageUrl())
                .hashtags(tip.getHashtags())
                .createdAt(tip.getCreatedAt())
                .build();
    }

    public static TipResponseDTO.GetAllTipResult toGetAllTip(Tip tip) {
        return TipResponseDTO.GetAllTipResult.builder()
                .id(tip.getId())
                .title(tip.getTitle())
                .imageUrl(tip.getImageUrl())
                .hashtags(tip.getHashtags())
                .build();
    }
}

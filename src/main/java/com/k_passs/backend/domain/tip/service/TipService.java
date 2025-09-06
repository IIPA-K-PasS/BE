package com.k_passs.backend.domain.tip.service;

import com.k_passs.backend.domain.tip.dto.TipResponseDTO;
import com.k_passs.backend.domain.tip.entity.Tip;

import java.util.List;

public interface TipService {
    TipResponseDTO.GetTipResult getTip(Long tipId);
    List<TipResponseDTO.GetAllTipResult> getAllTips();
}

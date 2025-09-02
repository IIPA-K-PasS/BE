package com.k_passs.backend.domain.tip.service;

import com.k_passs.backend.domain.tip.converter.TipConverter;
import com.k_passs.backend.domain.tip.dto.TipResponseDTO;
import com.k_passs.backend.domain.tip.entity.Tip;
import com.k_passs.backend.domain.tip.repository.TipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipServiceImpl implements TipService {

    private final TipRepository tipRepository;


    @Override
    @Transactional(readOnly = true)
    public TipResponseDTO.GetTipResult getTip(Long tipId) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팁이 존재하지 않습니다."));
        return TipConverter.toGetTip(tip);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipResponseDTO.GetAllTipResult> getAllTips() {
        return tipRepository.findAll().stream()
                .map(TipConverter::toGetAllTip)
                .toList();
    }
}

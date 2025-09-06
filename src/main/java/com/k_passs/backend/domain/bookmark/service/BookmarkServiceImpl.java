package com.k_passs.backend.domain.bookmark.service;

import com.k_passs.backend.domain.bookmark.dto.BookmarkRequestDTO;
import com.k_passs.backend.domain.bookmark.entity.Bookmark;
import com.k_passs.backend.domain.bookmark.repository.BookmarkRepository;
import com.k_passs.backend.domain.tip.entity.Tip;
import com.k_passs.backend.domain.tip.repository.TipRepository;
import com.k_passs.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final TipRepository tipRepository;

    // 북마크 상태 변환
    @Override
    @Transactional
    public void updateBookmarkStatus(User user, BookmarkRequestDTO.Bookmark bookmarkRequestDTO) {
        Long tipId = bookmarkRequestDTO.getTipId();
        boolean isBookmarked = bookmarkRequestDTO.getIsBookmarked();

        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팁이 존재하지 않습니다."));

        boolean exists = bookmarkRepository.existsByUserAndTip(user, tip);

        if (isBookmarked && !exists) {
            bookmarkRepository.save(new Bookmark(user, tip));
        } else if (!isBookmarked && exists) {
            bookmarkRepository.deleteByUserAndTip(user, tip);
        }
    }
}
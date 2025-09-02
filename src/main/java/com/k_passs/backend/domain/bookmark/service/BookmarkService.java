package com.k_passs.backend.domain.bookmark.service;

import com.k_passs.backend.domain.bookmark.dto.BookmarkResponseDTO;
import com.k_passs.backend.domain.tip.entity.Tip;
import com.k_passs.backend.domain.user.entity.User;

import java.util.List;

public interface BookmarkService {
    BookmarkResponseDTO.BookmarkResult updateBookmarkStatus(User user, Long tipId, boolean isBookmarked);
}
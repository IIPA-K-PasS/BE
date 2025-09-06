package com.k_passs.backend.domain.bookmark.service;

import com.k_passs.backend.domain.bookmark.dto.BookmarkRequestDTO;
import com.k_passs.backend.domain.user.entity.User;

public interface BookmarkService {
    void updateBookmarkStatus(User user, BookmarkRequestDTO.Bookmark bookmarkRequestDTO);
}
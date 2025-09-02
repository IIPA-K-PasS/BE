package com.k_passs.backend.domain.bookmark.repository;

import com.k_passs.backend.domain.bookmark.entity.Bookmark;
import com.k_passs.backend.domain.tip.entity.Tip;
import com.k_passs.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByUserAndTip(User user, Tip tip);
    void deleteByUserAndTip(User user, Tip tip);
    List<Bookmark> findByUser(User user);
}
package com.k_passs.backend.domain.user.repository;

import com.k_passs.backend.domain.bookmark.entity.Bookmark;
import com.k_passs.backend.domain.challenge.entity.Challenge;
import com.k_passs.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // 내가 찜한 꿀팁(Bookmarks) 조회
    @Query("select b from Bookmark b join fetch b.tip where b.user.id = :userId")
    List<Bookmark> findBookmarksByUserId(Long userId);

    // 완료한 챌린지 조회
    @Query("select c from Challenge c where c.user.id = :userId and c.isDone = true")
    List<Challenge> findCompletedChallengesByUserId(Long userId);
}

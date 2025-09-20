package com.k_passs.backend.domain.challenge.entity;

import com.k_passs.backend.domain.model.entity.BaseEntity;
import com.k_passs.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "challenges")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer rewardPoints;

    @Column(columnDefinition = "TEXT") // 긴 텍스트를 위해 TEXT 타입 사용
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isDone = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")   // Challenge 테이블에 user_id FK 존재
    private User user;
}
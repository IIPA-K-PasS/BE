package com.k_passs.backend.domain.tip.entity;

import com.k_passs.backend.domain.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tips")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tip extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 사진 URL
    @Column(length = 255)
    private String imageUrl;

    // 해시태그
    @Column(length = 255)
    private String hashtags;

    public Tip(String title, String content, String imageUrl, String hashtags, String infoText) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.hashtags = hashtags;
    }
}
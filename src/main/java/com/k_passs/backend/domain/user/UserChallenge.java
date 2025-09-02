package com.k_passs.backend.domain.user;

import com.k_passs.backend.domain.challenge.Challenge;
import com.k_passs.backend.domain.model.entity.BaseEntity;
import com.k_passs.backend.domain.model.enums.ChallengeStatus;
import com.k_passs.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_challenges")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChallenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeStatus status;

    private String proofImageUrl;
}

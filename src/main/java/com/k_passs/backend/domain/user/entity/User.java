package com.k_passs.backend.domain.user.entity;

import com.k_passs.backend.domain.bill.Bill;
import com.k_passs.backend.domain.model.entity.BaseEntity;
import com.k_passs.backend.domain.user.PointHistory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.k_passs.backend.domain.user.UserChallenge;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column
    private String profileImageUrl;

    @Column
    @Builder.Default
    private Long point = 0L; // 포인트 필드에 기본값 설정

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Bill> bills = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PointHistory> pointHistories = new ArrayList<>();

    // 이 부분은 사용자 테이블에는 없지만 연관관계를 고려해야 합니다.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserChallenge> userChallenges = new ArrayList<>();
}
//@Entity
//@Table(name = "users")
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Builder
//public class User extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true, length = 50)
//    private String nickname;
//
//    @Column(nullable = false, unique = true, length = 50)
//    private String email;
//
//    @Column
//    private String profileImageUrl;
//
//    @Column
//    private Long point;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Bill> bills = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<PointHistory> pointHistories = new ArrayList<>();
//}
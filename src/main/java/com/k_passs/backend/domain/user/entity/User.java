package com.k_passs.backend.domain.user.entity;

import com.k_passs.backend.domain.bill.entity.Bill;
import com.k_passs.backend.domain.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // DB 테이블명을 명시
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private Long point; // int보다 큰 값을 가질 수 있으므로 Long 타입 사용

    // User는 여러 Bill을 가질 수 있음 (1:N 관계)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Bill> bills = new ArrayList<>();

    // User는 여러 PointHistory를 가질 수 있음 (1:N 관계)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PointHistory> pointHistories = new ArrayList<>();
}
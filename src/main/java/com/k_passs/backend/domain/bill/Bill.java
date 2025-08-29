package com.k_passs.backend.domain.bill;

import com.k_passs.backend.domain.model.entity.BaseEntity;
import com.k_passs.backend.domain.model.enums.BillCategory;
import com.k_passs.backend.domain.model.enums.BillStatus;
import com.k_passs.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bills")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 최적화
    @JoinColumn(name = "member_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate periodMonth;

    @Enumerated(EnumType.STRING) // Enum 타입을 DB에 문자열로 저장
    @Column(nullable = false)
    private BillCategory category;

    // 금액은 부동소수점 오류를 피하기 위해 BigDecimal 사용
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillStatus status;

    @Column
    private String billImageUrl;
}

package com.k_passs.backend.domain.tip.repository;

import com.k_passs.backend.domain.tip.entity.Tip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipRepository extends JpaRepository<Tip, Long> {
}

package com.k_passs.backend.domain.bill.repository;

import com.k_passs.backend.domain.bill.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}

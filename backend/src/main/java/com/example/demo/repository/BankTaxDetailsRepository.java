package com.example.demo.repository;

import com.example.demo.entity.BankTaxDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankTaxDetailsRepository extends JpaRepository<BankTaxDetails, Long> {
    BankTaxDetails findByUserId(Long userId);
}
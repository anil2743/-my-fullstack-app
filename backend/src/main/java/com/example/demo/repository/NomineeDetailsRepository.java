package com.example.demo.repository;

import com.example.demo.entity.NomineeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NomineeDetailsRepository extends JpaRepository<NomineeDetails, Long> {
    List<NomineeDetails> findByUserId(Long userId);
}
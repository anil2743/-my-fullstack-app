package com.example.demo.repository;

import com.example.demo.entity.SchemeSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemeSelectionRepository extends JpaRepository<SchemeSelection, Long> {
    SchemeSelection findByUserId(Long userId);
}
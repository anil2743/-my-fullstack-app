package com.example.demo.repository;

import com.example.demo.entity.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long> {
    PersonalDetails findByUserId(Long userId);
}
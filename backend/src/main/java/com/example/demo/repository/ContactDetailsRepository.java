package com.example.demo.repository;

import com.example.demo.entity.ContactDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactDetailsRepository extends JpaRepository<ContactDetails, Long> {
    ContactDetails findByUserId(Long userId);
}
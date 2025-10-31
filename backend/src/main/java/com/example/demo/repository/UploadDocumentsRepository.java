package com.example.demo.repository;

import com.example.demo.entity.UploadDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadDocumentsRepository extends JpaRepository<UploadDocuments, Long> {
    UploadDocuments findByUserId(Long userId);
}
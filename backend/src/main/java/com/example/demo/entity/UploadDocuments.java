package com.example.demo.entity;

import com.example.demo.service.BinaryConversionService;
import jakarta.persistence.*;

@Entity
@Table(name = "upload_documents")
public class UploadDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "photo_file_data", columnDefinition = "LONGBLOB")
    private byte[] photoFileData;
    
    @Column(name = "signature_file_data", columnDefinition = "LONGBLOB")
    private byte[] signatureFileData;
    
    @Column(name = "subscriber_declaration")
    private String subscriberDeclaration;
    
    // JPA Lifecycle callbacks for binary conversion
    @PrePersist
    @PreUpdate
    public void convertToBinary() {
        this.subscriberDeclaration = BinaryConversionService.toBinary(this.subscriberDeclaration);
    }
    
    @PostLoad
    public void convertFromBinary() {
        this.subscriberDeclaration = BinaryConversionService.fromBinary(this.subscriberDeclaration);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public byte[] getPhotoFileData() { return photoFileData; }
    public void setPhotoFileData(byte[] photoFileData) { this.photoFileData = photoFileData; }
    
    public byte[] getSignatureFileData() { return signatureFileData; }
    public void setSignatureFileData(byte[] signatureFileData) { this.signatureFileData = signatureFileData; }
    
    public String getSubscriberDeclaration() { return subscriberDeclaration; }
    public void setSubscriberDeclaration(String subscriberDeclaration) { this.subscriberDeclaration = subscriberDeclaration; }
}
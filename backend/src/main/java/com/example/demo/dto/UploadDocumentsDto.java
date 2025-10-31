package com.example.demo.dto;

public class UploadDocumentsDto {
    private String photoFileData;
    private String signatureFileData;
    private String subscriberDeclaration;
    
    // Getters and Setters
    public String getPhotoFileData() { return photoFileData; }
    public void setPhotoFileData(String photoFileData) { this.photoFileData = photoFileData; }
    
    public String getSignatureFileData() { return signatureFileData; }
    public void setSignatureFileData(String signatureFileData) { this.signatureFileData = signatureFileData; }
    
    public String getSubscriberDeclaration() { return subscriberDeclaration; }
    public void setSubscriberDeclaration(String subscriberDeclaration) { this.subscriberDeclaration = subscriberDeclaration; }
}
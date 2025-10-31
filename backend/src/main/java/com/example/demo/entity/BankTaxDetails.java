package com.example.demo.entity;

import com.example.demo.service.BinaryConversionService;
import jakarta.persistence.*;

@Entity
@Table(name = "bank_tax_details")
public class BankTaxDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "account_type")
    private String accountType;
    
    @Column(name = "account_number", unique = true)
    private String accountNumber;
    
    @Column(name = "ifsc_code")
    private String ifscCode;
    
    @Column(name = "bank_name")
    private String bankName;
    
    @Column(name = "is_us_person")
    private String isUSPerson;
    
    @Column(name = "tax_residency")
    private String taxResidency;
    
    @Column(name = "tax_id")
    private String taxId;
    
    // JPA Lifecycle callbacks for binary conversion
    @PrePersist
    @PreUpdate
    public void convertToBinary() {
        this.accountType = BinaryConversionService.toBinary(this.accountType);
        this.accountNumber = BinaryConversionService.toBinary(this.accountNumber);
        this.ifscCode = BinaryConversionService.toBinary(this.ifscCode);
        this.bankName = BinaryConversionService.toBinary(this.bankName);
        this.isUSPerson = BinaryConversionService.toBinary(this.isUSPerson);
        this.taxResidency = BinaryConversionService.toBinary(this.taxResidency);
        this.taxId = BinaryConversionService.toBinary(this.taxId);
    }
    
    @PostLoad
    public void convertFromBinary() {
        this.accountType = BinaryConversionService.fromBinary(this.accountType);
        this.accountNumber = BinaryConversionService.fromBinary(this.accountNumber);
        this.ifscCode = BinaryConversionService.fromBinary(this.ifscCode);
        this.bankName = BinaryConversionService.fromBinary(this.bankName);
        this.isUSPerson = BinaryConversionService.fromBinary(this.isUSPerson);
        this.taxResidency = BinaryConversionService.fromBinary(this.taxResidency);
        this.taxId = BinaryConversionService.fromBinary(this.taxId);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
    
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    
    public String getIsUSPerson() { return isUSPerson; }
    public void setIsUSPerson(String isUSPerson) { this.isUSPerson = isUSPerson; }
    
    public String getTaxResidency() { return taxResidency; }
    public void setTaxResidency(String taxResidency) { this.taxResidency = taxResidency; }
    
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
}
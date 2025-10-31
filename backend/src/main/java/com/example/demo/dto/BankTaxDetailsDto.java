package com.example.demo.dto;

public class BankTaxDetailsDto {
    private String accountType;
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String isUSPerson;
    private String taxResidency;
    private String taxId;
    
    // Getters and Setters
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
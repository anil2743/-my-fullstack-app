package com.example.demo.entity;

import com.example.demo.service.BinaryConversionService;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "contact_details")
public class ContactDetails {

    private static final Logger logger = LoggerFactory.getLogger(ContactDetails.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "mobile", unique = true, length = 128)
    private String mobile;
    
    @Column(name = "email", unique = true, length = 254)
    private String email;
    
    @Column(name = "citizen_flag", length = 32)
    private String citizenFlag;
    
    @Lob
    @Column(name = "p_addr_line1", columnDefinition = "TEXT")
    private String pAddrLine1;
    
    @Lob
    @Column(name = "p_addr_line2", columnDefinition = "TEXT")
    private String pAddrLine2;
    
    @Lob
    @Column(name = "p_addr_line4", columnDefinition = "TEXT")
    private String pAddrLine4;
    
    @Column(name = "p_state", length = 255)
    private String pState;
    
    @Column(name = "p_pincode", length = 64)
    private String pPincode;
    
    @Column(name = "p_country", length = 255)
    private String pCountry;
    
    @Lob
    @Column(name = "c_addr_line1", columnDefinition = "TEXT")
    private String cAddrLine1;
    
    @Lob
    @Column(name = "c_addr_line2", columnDefinition = "TEXT")
    private String cAddrLine2;
    
    @Lob
    @Column(name = "c_addr_line4", columnDefinition = "TEXT")
    private String cAddrLine4;
    
    @Column(name = "c_state", length = 255)
    private String cState;
    
    @Column(name = "c_postal_code", length = 255)
    private String cPostalCode;
    
    @Column(name = "c_country", length = 255)
    private String cCountry;

    @PrePersist
    @PreUpdate
    public void convertToBase64() {
        logger.info("CONVERSION START: Converting fields to Base64 for user_id: {}", this.userId);
        this.mobile = BinaryConversionService.toBinary(this.mobile);
        this.email = BinaryConversionService.toBinary(this.email);
        this.citizenFlag = BinaryConversionService.toBinary(this.citizenFlag);
        this.pAddrLine1 = BinaryConversionService.toBinary(this.pAddrLine1);
        this.pAddrLine2 = BinaryConversionService.toBinary(this.pAddrLine2);
        this.pAddrLine4 = BinaryConversionService.toBinary(this.pAddrLine4);
        this.pState = BinaryConversionService.toBinary(this.pState);
        this.pPincode = BinaryConversionService.toBinary(this.pPincode);
        this.pCountry = BinaryConversionService.toBinary(this.pCountry);
        this.cAddrLine1 = BinaryConversionService.toBinary(this.cAddrLine1);
        this.cAddrLine2 = BinaryConversionService.toBinary(this.cAddrLine2);
        this.cAddrLine4 = BinaryConversionService.toBinary(this.cAddrLine4);
        this.cState = BinaryConversionService.toBinary(this.cState);
        this.cPostalCode = BinaryConversionService.toBinary(this.cPostalCode);
        this.cCountry = BinaryConversionService.toBinary(this.cCountry);
        logger.info("CONVERSION END: All fields converted for user_id: {}", this.userId);
    }
    
    @PostLoad
    public void convertFromBase64() {
        logger.debug("Converting fields from Base64 for user_id: {}", this.userId);
        this.mobile = BinaryConversionService.fromBinary(this.mobile);
        this.email = BinaryConversionService.fromBinary(this.email);
        this.citizenFlag = BinaryConversionService.fromBinary(this.citizenFlag);
        this.pAddrLine1 = BinaryConversionService.fromBinary(this.pAddrLine1);
        this.pAddrLine2 = BinaryConversionService.fromBinary(this.pAddrLine2);
        this.pAddrLine4 = BinaryConversionService.fromBinary(this.pAddrLine4);
        this.pState = BinaryConversionService.fromBinary(this.pState);
        this.pPincode = BinaryConversionService.fromBinary(this.pPincode);
        this.pCountry = BinaryConversionService.fromBinary(this.pCountry);
        this.cAddrLine1 = BinaryConversionService.fromBinary(this.cAddrLine1);
        this.cAddrLine2 = BinaryConversionService.fromBinary(this.cAddrLine2);
        this.cAddrLine4 = BinaryConversionService.fromBinary(this.cAddrLine4);
        this.cState = BinaryConversionService.fromBinary(this.cState);
        this.cPostalCode = BinaryConversionService.fromBinary(this.cPostalCode);
        this.cCountry = BinaryConversionService.fromBinary(this.cCountry);
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCitizenFlag() { return citizenFlag; }
    public void setCitizenFlag(String citizenFlag) { this.citizenFlag = citizenFlag; }
    public String getPAddrLine1() { return pAddrLine1; }
    public void setPAddrLine1(String pAddrLine1) { this.pAddrLine1 = pAddrLine1; }
    public String getPAddrLine2() { return pAddrLine2; }
    public void setPAddrLine2(String pAddrLine2) { this.pAddrLine2 = pAddrLine2; }
    public String getPAddrLine4() { return pAddrLine4; }
    public void setPAddrLine4(String pAddrLine4) { this.pAddrLine4 = pAddrLine4; }
    public String getPState() { return pState; }
    public void setPState(String pState) { this.pState = pState; }
    public String getPPincode() { return pPincode; }
    public void setPPincode(String pPincode) { this.pPincode = pPincode; }
    public String getPCountry() { return pCountry; }
    public void setPCountry(String pCountry) { this.pCountry = pCountry; }
    public String getCAddrLine1() { return cAddrLine1; }
    public void setCAddrLine1(String cAddrLine1) { this.cAddrLine1 = cAddrLine1; }
    public String getCAddrLine2() { return cAddrLine2; }
    public void setCAddrLine2(String cAddrLine2) { this.cAddrLine2 = cAddrLine2; }
    public String getCAddrLine4() { return cAddrLine4; }
    public void setCAddrLine4(String cAddrLine4) { this.cAddrLine4 = cAddrLine4; }
    public String getCState() { return cState; }
    public void setCState(String cState) { this.cState = cState; }
    public String getCPostalCode() { return cPostalCode; }
    public void setCPostalCode(String cPostalCode) { this.cPostalCode = cPostalCode; }
    public String getCCountry() { return cCountry; }
    public void setCCountry(String cCountry) { this.cCountry = cCountry; }
}
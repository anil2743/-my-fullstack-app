package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactDetailsDto {
    private String mobile;
    private String email;
    private String citizenFlag;

    @JsonProperty("pAddrLine1")
    private String pAddrLine1;

    @JsonProperty("pAddrLine2")
    private String pAddrLine2;

    @JsonProperty("pAddrLine4")
    private String pAddrLine4;

    @JsonProperty("pState")
    private String pState;

    @JsonProperty("pPincode")
    private String pPincode;

    @JsonProperty("pCountry")
    private String pCountry;

    @JsonProperty("cAddrLine1")
    private String cAddrLine1;

    @JsonProperty("cAddrLine2")
    private String cAddrLine2;

    @JsonProperty("cAddrLine4")
    private String cAddrLine4;

    @JsonProperty("cState")
    private String cState;

    @JsonProperty("cPostalCode")
    private String cPostalCode;

    @JsonProperty("cCountry")
    private String cCountry;
    
    // --- Getters and Setters ---
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
    
    @Override
    public String toString() {
        return "ContactDetailsDto{" +
                "mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", citizenFlag='" + citizenFlag + '\'' +
                ", pAddrLine1='" + pAddrLine1 + '\'' +
                ", pAddrLine2='" + pAddrLine2 + '\'' +
                ", pAddrLine4='" + pAddrLine4 + '\'' +
                ", pState='" + pState + '\'' +
                ", pPincode='" + pPincode + '\'' +
                ", pCountry='" + pCountry + '\'' +
                ", cAddrLine1='" + cAddrLine1 + '\'' +
                ", cAddrLine2='" + cAddrLine2 + '\'' +
                ", cAddrLine4='" + cAddrLine4 + '\'' +
                ", cState='" + cState + '\'' +
                ", cPostalCode='" + cPostalCode + '\'' +
                ", cCountry='" + cCountry + '\'' +
                '}';
    }
}
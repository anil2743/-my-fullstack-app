package com.example.demo.entity;

import com.example.demo.service.BinaryConversionService;
import jakarta.persistence.*;

@Entity
@Table(name = "nominee_details")
public class NomineeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "nominee_id")
    private Integer nomineeId;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "relationship")
    private String relationship;
    
    @Column(name = "date_of_birth")
    private String dateOfBirth;
    
    @Column(name = "percentage_share")
    private Integer percentageShare;
    
    @Column(name = "minor_nominee_flag")
    private String minorNomineeFlag;
    
    @Column(name = "guardian_name")
    private String guardianName;
    
    @Column(name = "guardian_relationship")
    private String guardianRelationship;
    
    // JPA Lifecycle callbacks for binary conversion
    @PrePersist
    @PreUpdate
    public void convertToBinary() {
        this.firstName = BinaryConversionService.toBinary(this.firstName);
        this.lastName = BinaryConversionService.toBinary(this.lastName);
        this.relationship = BinaryConversionService.toBinary(this.relationship);
        this.dateOfBirth = BinaryConversionService.toBinary(this.dateOfBirth);
        this.minorNomineeFlag = BinaryConversionService.toBinary(this.minorNomineeFlag);
        this.guardianName = BinaryConversionService.toBinary(this.guardianName);
        this.guardianRelationship = BinaryConversionService.toBinary(this.guardianRelationship);
    }
    
    @PostLoad
    public void convertFromBinary() {
        this.firstName = BinaryConversionService.fromBinary(this.firstName);
        this.lastName = BinaryConversionService.fromBinary(this.lastName);
        this.relationship = BinaryConversionService.fromBinary(this.relationship);
        this.dateOfBirth = BinaryConversionService.fromBinary(this.dateOfBirth);
        this.minorNomineeFlag = BinaryConversionService.fromBinary(this.minorNomineeFlag);
        this.guardianName = BinaryConversionService.fromBinary(this.guardianName);
        this.guardianRelationship = BinaryConversionService.fromBinary(this.guardianRelationship);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Integer getNomineeId() { return nomineeId; }
    public void setNomineeId(Integer nomineeId) { this.nomineeId = nomineeId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public Integer getPercentageShare() { return percentageShare; }
    public void setPercentageShare(Integer percentageShare) { this.percentageShare = percentageShare; }
    
    public String getMinorNomineeFlag() { return minorNomineeFlag; }
    public void setMinorNomineeFlag(String minorNomineeFlag) { this.minorNomineeFlag = minorNomineeFlag; }
    
    public String getGuardianName() { return guardianName; }
    public void setGuardianName(String guardianName) { this.guardianName = guardianName; }
    
    public String getGuardianRelationship() { return guardianRelationship; }
    public void setGuardianRelationship(String guardianRelationship) { this.guardianRelationship = guardianRelationship; }
}
package com.example.demo.entity;

import com.example.demo.service.BinaryConversionService;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "personal_details")
public class PersonalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "middle_name")
    private String middleName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "dob")
    private LocalDate dob;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "fathers_first_name")
    private String fathersFirstName;
    
    @Column(name = "mothers_first_name")
    private String mothersFirstName;
    
    @Column(name = "pan", unique = true)
    private String pan;
    
    // JPA Lifecycle callbacks for binary conversion
    @PrePersist
    @PreUpdate
    public void convertToBinary() {
        this.title = BinaryConversionService.toBinary(this.title);
        this.firstName = BinaryConversionService.toBinary(this.firstName);
        this.middleName = BinaryConversionService.toBinary(this.middleName);
        this.lastName = BinaryConversionService.toBinary(this.lastName);
        this.gender = BinaryConversionService.toBinary(this.gender);
        this.fathersFirstName = BinaryConversionService.toBinary(this.fathersFirstName);
        this.mothersFirstName = BinaryConversionService.toBinary(this.mothersFirstName);
        this.pan = BinaryConversionService.toBinary(this.pan);
    }
    
    @PostLoad
    public void convertFromBinary() {
        this.title = BinaryConversionService.fromBinary(this.title);
        this.firstName = BinaryConversionService.fromBinary(this.firstName);
        this.middleName = BinaryConversionService.fromBinary(this.middleName);
        this.lastName = BinaryConversionService.fromBinary(this.lastName);
        this.gender = BinaryConversionService.fromBinary(this.gender);
        this.fathersFirstName = BinaryConversionService.fromBinary(this.fathersFirstName);
        this.mothersFirstName = BinaryConversionService.fromBinary(this.mothersFirstName);
        this.pan = BinaryConversionService.fromBinary(this.pan);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getFathersFirstName() { return fathersFirstName; }
    public void setFathersFirstName(String fathersFirstName) { this.fathersFirstName = fathersFirstName; }
    
    public String getMothersFirstName() { return mothersFirstName; }
    public void setMothersFirstName(String mothersFirstName) { this.mothersFirstName = mothersFirstName; }
    
    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }
}
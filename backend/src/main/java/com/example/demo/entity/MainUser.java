package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "main_user")
public class MainUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "registration_date")
    private java.time.LocalDateTime registrationDate;
    
    @Column(name = "status")
    private String status;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public java.time.LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(java.time.LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
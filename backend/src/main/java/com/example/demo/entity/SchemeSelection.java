package com.example.demo.entity;

import com.example.demo.service.BinaryConversionService;
import jakarta.persistence.*;

@Entity
@Table(name = "scheme_selection")
public class SchemeSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "scheme_choice")
    private String schemeChoice;
    
    @Column(name = "life_cycle_fund")
    private String lifeCycleFund;
    
    // JPA Lifecycle callbacks for binary conversion
    @PrePersist
    @PreUpdate
    public void convertToBinary() {
        this.schemeChoice = BinaryConversionService.toBinary(this.schemeChoice);
        this.lifeCycleFund = BinaryConversionService.toBinary(this.lifeCycleFund);
    }
    
    @PostLoad
    public void convertFromBinary() {
        this.schemeChoice = BinaryConversionService.fromBinary(this.schemeChoice);
        this.lifeCycleFund = BinaryConversionService.fromBinary(this.lifeCycleFund);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getSchemeChoice() { return schemeChoice; }
    public void setSchemeChoice(String schemeChoice) { this.schemeChoice = schemeChoice; }
    
    public String getLifeCycleFund() { return lifeCycleFund; }
    public void setLifeCycleFund(String lifeCycleFund) { this.lifeCycleFund = lifeCycleFund; }
}
package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.MainUser;
import com.example.demo.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/personal-details")
    public ResponseEntity<?> savePersonalDetails(@RequestBody PersonalDetailsDto personalDetailsDto, @RequestParam(required = false) Long userId) {
        try {
            Long savedUserId;
            if (userId != null) {
                // Update existing user
                savedUserId = registrationService.savePersonalDetails(personalDetailsDto, userId);
            } else {
                // Create new user
                savedUserId = registrationService.savePersonalDetails(personalDetailsDto);
            }
            return ResponseEntity.ok(savedUserId);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already registered")) {
                return ResponseEntity.status(409).body(e.getMessage()); // 409 Conflict
            }
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            // Handle database constraint violations
            if (e.getCause() != null && e.getCause().getMessage().contains("Duplicate entry")) {
                return ResponseEntity.status(409).body("PAN already registered. Please use a different PAN or contact support.");
            }
            return ResponseEntity.status(500).body("Error saving personal details. Please try again.");
        }
    }

    @GetMapping("/personal-details")
    public ResponseEntity<?> getPersonalDetails(@RequestParam Long userId) {
        try {
            PersonalDetailsDto details = registrationService.getPersonalDetails(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching personal details");
        }
    }

    @PostMapping("/check-duplicates")
    public ResponseEntity<?> checkDuplicates(@RequestBody Map<String, String> request) {
        Map<String, String> errors = new HashMap<>();
        
        String pan = request.get("pan");
        String email = request.get("email");
        String mobile = request.get("mobile");
        String accountNumber = request.get("accountNumber");
        
        if (pan != null && !pan.trim().isEmpty()) {
            if (registrationService.isPanAlreadyRegistered(pan)) {
                errors.put("pan", "PAN already registered");
            }
        }
        
        if (email != null && !email.trim().isEmpty()) {
            if (registrationService.isEmailAlreadyRegistered(email)) {
                errors.put("email", "Email already registered");
            }
        }
        
        if (mobile != null && !mobile.trim().isEmpty()) {
            if (registrationService.isMobileAlreadyRegistered(mobile)) {
                errors.put("mobile", "Mobile number already registered");
            }
        }
        
        if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            if (registrationService.isAccountNumberAlreadyRegistered(accountNumber)) {
                errors.put("accountNumber", "Bank account number already registered");
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("hasErrors", !errors.isEmpty());
        response.put("errors", errors);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/contact-details")
    public ResponseEntity<?> saveContactDetails(@RequestBody ContactDetailsDto contactDetailsDto, @RequestParam Long userId) {
        try {
            Long savedUserId = registrationService.saveContactDetails(contactDetailsDto, userId);
            return ResponseEntity.ok(savedUserId);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already registered")) {
                return ResponseEntity.status(409).body(e.getMessage());
            }
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving contact details. Please try again.");
        }
    }

    @GetMapping("/contact-details")
    public ResponseEntity<?> getContactDetails(@RequestParam Long userId) {
        try {
            ContactDetailsDto details = registrationService.getContactDetails(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching contact details");
        }
    }

    @PostMapping("/bank-tax-details")
    public ResponseEntity<?> saveBankTaxDetails(@RequestBody BankTaxDetailsDto bankTaxDetailsDto, @RequestParam Long userId) {
        try {
            Long savedUserId = registrationService.saveBankTaxDetails(bankTaxDetailsDto, userId);
            return ResponseEntity.ok(savedUserId);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already registered")) {
                return ResponseEntity.status(409).body(e.getMessage());
            }
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving bank tax details. Please try again.");
        }
    }

    @GetMapping("/bank-tax-details")
    public ResponseEntity<?> getBankTaxDetails(@RequestParam Long userId) {
        try {
            BankTaxDetailsDto details = registrationService.getBankTaxDetails(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching bank tax details");
        }
    }

    @PostMapping("/scheme-selection")
    public ResponseEntity<Long> saveSchemeSelection(@RequestBody SchemeSelectionDto schemeSelectionDto, @RequestParam Long userId) {
        Long savedUserId = registrationService.saveSchemeSelection(schemeSelectionDto, userId);
        return ResponseEntity.ok(savedUserId);
    }

    @GetMapping("/scheme-selection")
    public ResponseEntity<?> getSchemeSelection(@RequestParam Long userId) {
        try {
            SchemeSelectionDto details = registrationService.getSchemeSelection(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching scheme selection");
        }
    }

    @PostMapping("/nominee-details")
    public ResponseEntity<Long> saveNomineeDetails(@RequestBody NomineeDetailsDto nomineeDetailsDto, @RequestParam Long userId) {
        Long savedUserId = registrationService.saveNomineeDetails(nomineeDetailsDto, userId);
        return ResponseEntity.ok(savedUserId);
    }

    @GetMapping("/nominee-details")
    public ResponseEntity<?> getNomineeDetails(@RequestParam Long userId) {
        try {
            NomineeDetailsDto details = registrationService.getNomineeDetails(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching nominee details");
        }
    }

    @PostMapping("/upload-documents")
    public ResponseEntity<Long> saveUploadDocuments(@RequestBody UploadDocumentsDto uploadDocumentsDto, @RequestParam Long userId) {
        Long savedUserId = registrationService.saveUploadDocuments(uploadDocumentsDto, userId);
        registrationService.completeRegistration(userId);
        registrationService.addToDuplicateAfterRegistration(userId);
        return ResponseEntity.ok(savedUserId);
    }

    @GetMapping("/upload-documents")
    public ResponseEntity<?> getUploadDocuments(@RequestParam Long userId) {
        try {
            UploadDocumentsDto details = registrationService.getUploadDocuments(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching upload documents");
        }
    }

    @GetMapping("/user-status")
    public ResponseEntity<?> getUserStatus(@RequestParam Long userId) {
        try {
            MainUser user = registrationService.getUserById(userId);
            if (user != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", user.getStatus());
                response.put("registrationDate", user.getRegistrationDate());
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching user status");
        }
    }
}
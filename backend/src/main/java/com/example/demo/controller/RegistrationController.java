package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.MainUser;
import com.example.demo.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")  // Changed from "/api/registration" to "/api"
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private RegistrationService registrationService;

    // This endpoint is now at /api/check-pan
    @PostMapping("/check-pan")
    public ResponseEntity<?> checkPan(@RequestBody Map<String, String> request) {
        try {
            String pan = request.get("pan");
            if (pan == null || pan.trim().isEmpty() || !pan.matches("[A-Z]{5}\\d{4}[A-Z]{1}")) {
                return ResponseEntity.badRequest().body(Map.of("valid", false, "message", "Invalid PAN format"));
            }
            boolean exists = registrationService.isPanAlreadyRegistered(pan);
            return ResponseEntity.ok(Map.of("valid", true, "exists", exists));
        } catch (Exception e) {
            logger.error("Error checking PAN: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("valid", false, "message", "Error checking PAN"));
        }
    }

    // This endpoint is now at /api/personal-details
    @PostMapping("/personal-details")
    public ResponseEntity<?> savePersonalDetails(@RequestBody @Validated PersonalDetailsDto personalDetailsDto, @RequestParam(required = false) Long userId) {
        try {
            Long savedUserId;
            if (userId != null) {
                savedUserId = registrationService.savePersonalDetails(personalDetailsDto, userId);
            } else {
                savedUserId = registrationService.savePersonalDetails(personalDetailsDto);
            }
            return ResponseEntity.ok(savedUserId);
        } catch (RuntimeException e) {
            logger.error("Runtime error saving personal details: {}", e.getMessage());
            if (e.getMessage().contains("already registered")) {
                return ResponseEntity.status(409).body(e.getMessage()); // 409 Conflict
            }
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error saving personal details: {}", e.getMessage(), e);
            if (e.getCause() != null && e.getCause().getMessage().contains("Duplicate entry")) {
                return ResponseEntity.status(409).body("PAN already registered. Please use a different PAN or contact support.");
            }
            return ResponseEntity.status(500).body("Error saving personal details. Please try again.");
        }
    }

    // This endpoint is now at /api/personal-details
    @GetMapping("/personal-details")
    public ResponseEntity<?> getPersonalDetails(@RequestParam Long userId) {
        try {
            PersonalDetailsDto details = registrationService.getPersonalDetails(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching personal details: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error fetching personal details");
        }
    }

    // This endpoint is now at /api/check-duplicates
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

    // This endpoint is now at /api/contact-details
    @PostMapping("/contact-details")
    public ResponseEntity<?> saveContactDetails(@RequestBody @Validated ContactDetailsDto contactDetailsDto, @RequestParam Long userId) {
        try {
            Long savedUserId = registrationService.saveContactDetails(contactDetailsDto, userId);
            return ResponseEntity.ok(savedUserId);
        } catch (RuntimeException e) {
            logger.error("Runtime error saving contact details: {}", e.getMessage());
            if (e.getMessage().contains("already registered")) {
                return ResponseEntity.status(409).body(e.getMessage()); // FIXED: 409 for duplicates instead of 400
            }
            return ResponseEntity.status(400).body(e.getMessage()); // Keep 400 for other validation
        } catch (Exception e) {
            logger.error("Error saving contact details: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error saving contact details. Please try again.");
        }
    }

    // This endpoint is now at /api/contact-details
    @GetMapping("/contact-details")
    public ResponseEntity<?> getContactDetails(@RequestParam Long userId) {
        try {
            ContactDetailsDto details = registrationService.getContactDetails(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching contact details: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error fetching contact details");
        }
    }

    // This endpoint is now at /api/bank-tax-details
    @PostMapping("/bank-tax-details")
    public ResponseEntity<?> saveBankTaxDetails(@RequestBody @Validated BankTaxDetailsDto bankTaxDetailsDto, @RequestParam Long userId) {
        try {
            Long savedUserId = registrationService.saveBankTaxDetails(bankTaxDetailsDto, userId);
            return ResponseEntity.ok(savedUserId);
        } catch (RuntimeException e) {
            logger.error("Runtime error saving bank tax details: {}", e.getMessage());
            if (e.getMessage().contains("already registered")) {
                return ResponseEntity.status(409).body(e.getMessage());
            }
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error saving bank tax details: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error saving bank tax details. Please try again.");
        }
    }

    // This endpoint is now at /api/bank-tax-details
    @GetMapping("/bank-tax-details")
    public ResponseEntity<?> getBankTaxDetails(@RequestParam Long userId) {
        try {
            BankTaxDetailsDto details = registrationService.getBankTaxDetails(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching bank tax details: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error fetching bank tax details");
        }
    }

    // This endpoint is now at /api/scheme-selection
    @PostMapping("/scheme-selection")
    public ResponseEntity<Long> saveSchemeSelection(@RequestBody SchemeSelectionDto schemeSelectionDto, @RequestParam Long userId) {
        Long savedUserId = registrationService.saveSchemeSelection(schemeSelectionDto, userId);
        return ResponseEntity.ok(savedUserId);
    }

    // This endpoint is now at /api/scheme-selection
    @GetMapping("/scheme-selection")
    public ResponseEntity<?> getSchemeSelection(@RequestParam Long userId) {
        try {
            SchemeSelectionDto details = registrationService.getSchemeSelection(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching scheme selection: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error fetching scheme selection");
        }
    }

    // This endpoint is now at /api/nominee-details
    @PostMapping("/nominee-details")
    public ResponseEntity<Long> saveNomineeDetails(@RequestBody NomineeDetailsDto nomineeDetailsDto, @RequestParam Long userId) {
        Long savedUserId = registrationService.saveNomineeDetails(nomineeDetailsDto, userId);
        return ResponseEntity.ok(savedUserId);
    }

    // This endpoint is now at /api/nominee-details
    @GetMapping("/nominee-details")
    public ResponseEntity<?> getNomineeDetails(@RequestParam Long userId) {
        try {
            NomineeDetailsDto details = registrationService.getNomineeDetails(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching nominee details: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error fetching nominee details");
        }
    }

    // This endpoint is now at /api/upload-documents
    @PostMapping("/upload-documents")
    public ResponseEntity<Long> saveUploadDocuments(@RequestBody UploadDocumentsDto uploadDocumentsDto, @RequestParam Long userId) {
        Long savedUserId = registrationService.saveUploadDocuments(uploadDocumentsDto, userId);
        registrationService.completeRegistration(userId);
        registrationService.addToDuplicateAfterRegistration(userId);
        return ResponseEntity.ok(savedUserId);
    }

    // This endpoint is now at /api/upload-documents
    @GetMapping("/upload-documents")
    public ResponseEntity<?> getUploadDocuments(@RequestParam Long userId) {
        try {
            UploadDocumentsDto details = registrationService.getUploadDocuments(userId);
            if (details != null) {
                return ResponseEntity.ok(details);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching upload documents: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error fetching upload documents");
        }
    }

    // This endpoint is now at /api/user-status
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
            logger.error("Error fetching user status: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error fetching user status");
        }
    }
}
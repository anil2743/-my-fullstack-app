package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

@Service
public class RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    private MainUserRepository mainUserRepository;
    
    @Autowired
    private PersonalDetailsRepository personalDetailsRepository;
    
    @Autowired
    private ContactDetailsRepository contactDetailsRepository;
    
    @Autowired
    private BankTaxDetailsRepository bankTaxDetailsRepository;
    
    @Autowired
    private SchemeSelectionRepository schemeSelectionRepository;
    
    @Autowired
    private NomineeDetailsRepository nomineeDetailsRepository;
    
    @Autowired
    private UploadDocumentsRepository uploadDocumentsRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Check if PAN is already registered in duplicate table
    public boolean isPanAlreadyRegistered(String pan) {
        if (pan == null || pan.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Get all binary PANs from duplicate table
            String sql = "SELECT pan FROM duplicate WHERE pan IS NOT NULL";
            List<String> binaryPans = jdbcTemplate.queryForList(sql, String.class);
            
            // Convert each PAN from binary and compare with user's PAN
            String userPan = pan.trim().toUpperCase();
            for (String binaryPan : binaryPans) {
                try {
                    String decryptedPan = BinaryConversionService.fromBinary(binaryPan);
                    if (userPan.equals(decryptedPan)) {
                        logger.info("PAN already registered in duplicate table: {}", userPan);
                        return true;
                    }
                } catch (Exception e) {
                    // Skip invalid binary values
                    logger.debug("Error converting PAN from binary: " + e.getMessage());
                }
            }
            
            return false;
        } catch (Exception e) {
            logger.error("Error checking PAN duplicate in duplicate table: " + e.getMessage());
            return false;
        }
    }

    // Check if PAN is already registered in personal_details table
    public boolean isPanAlreadyRegisteredInPersonalDetails(String pan, Long excludeUserId) {
        if (pan == null || pan.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Convert PAN to binary for comparison
            String binaryPan = BinaryConversionService.toBinary(pan.trim().toUpperCase());
            
            // Check if PAN exists in personal_details table (excluding current user if updating)
            String sql = excludeUserId != null 
                ? "SELECT COUNT(*) FROM personal_details WHERE pan = ? AND user_id != ?"
                : "SELECT COUNT(*) FROM personal_details WHERE pan = ?";
                
            Integer count = excludeUserId != null
                ? jdbcTemplate.queryForObject(sql, Integer.class, binaryPan, excludeUserId)
                : jdbcTemplate.queryForObject(sql, Integer.class, binaryPan);
                
            return count != null && count > 0;
        } catch (Exception e) {
            logger.error("Error checking PAN duplicate in personal_details: " + e.getMessage());
            return false;
        }
    }

    // Check if email is already registered in duplicate table
    public boolean isEmailAlreadyRegistered(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Get all binary emails from duplicate table
            String sql = "SELECT email FROM duplicate WHERE email IS NOT NULL";
            List<String> binaryEmails = jdbcTemplate.queryForList(sql, String.class);
            
            // Convert each email from binary and compare with user's email
            String userEmail = email.trim().toLowerCase();
            for (String binaryEmail : binaryEmails) {
                try {
                    String decryptedEmail = BinaryConversionService.fromBinary(binaryEmail);
                    if (userEmail.equals(decryptedEmail.toLowerCase())) {
                        logger.info("Email already registered in duplicate table: {}", userEmail);
                        return true;
                    }
                } catch (Exception e) {
                    // Skip invalid binary values
                    logger.debug("Error converting email from binary: " + e.getMessage());
                }
            }
            
            return false;
        } catch (Exception e) {
            logger.error("Error checking email duplicate in duplicate table: " + e.getMessage());
            return false;
        }
    }

    // Check if mobile is already registered in duplicate table
    public boolean isMobileAlreadyRegistered(String mobile) {
        if (mobile == null || mobile.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Get all binary mobiles from duplicate table
            String sql = "SELECT mobile FROM duplicate WHERE mobile IS NOT NULL";
            List<String> binaryMobiles = jdbcTemplate.queryForList(sql, String.class);
            
            // Convert each mobile from binary and compare with user's mobile
            String userMobile = mobile.trim();
            for (String binaryMobile : binaryMobiles) {
                try {
                    String decryptedMobile = BinaryConversionService.fromBinary(binaryMobile);
                    if (userMobile.equals(decryptedMobile)) {
                        logger.info("Mobile already registered in duplicate table: {}", userMobile);
                        return true;
                    }
                } catch (Exception e) {
                    // Skip invalid binary values
                    logger.debug("Error converting mobile from binary: " + e.getMessage());
                }
            }
            
            return false;
        } catch (Exception e) {
            logger.error("Error checking mobile duplicate in duplicate table: " + e.getMessage());
            return false;
        }
    }

    // Check if account number is already registered in duplicate table
    public boolean isAccountNumberAlreadyRegistered(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Get all binary account numbers from duplicate table
            String sql = "SELECT account_number FROM duplicate WHERE account_number IS NOT NULL";
            List<String> binaryAccountNumbers = jdbcTemplate.queryForList(sql, String.class);
            
            // Convert each account number from binary and compare with user's account number
            String userAccountNumber = accountNumber.trim();
            for (String binaryAccountNumber : binaryAccountNumbers) {
                try {
                    String decryptedAccountNumber = BinaryConversionService.fromBinary(binaryAccountNumber);
                    if (userAccountNumber.equals(decryptedAccountNumber)) {
                        logger.info("Account number already registered in duplicate table: {}", userAccountNumber);
                        return true;
                    }
                } catch (Exception e) {
                    // Skip invalid binary values
                    logger.debug("Error converting account number from binary: " + e.getMessage());
                }
            }
            
            return false;
        } catch (Exception e) {
            logger.error("Error checking account number duplicate in duplicate table: " + e.getMessage());
            return false;
        }
    }

    // Add all details to duplicate table
    private void addToDuplicateTable(String pan, String email, String mobile, String accountNumber) {
        try {
            String sql = "INSERT INTO duplicate (pan, email, mobile, account_number, registration_date) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                pan != null ? BinaryConversionService.toBinary(pan.trim().toUpperCase()) : null,
                email != null ? BinaryConversionService.toBinary(email.trim().toLowerCase()) : null,
                mobile != null ? BinaryConversionService.toBinary(mobile.trim()) : null,
                accountNumber != null ? BinaryConversionService.toBinary(accountNumber.trim()) : null,
                LocalDateTime.now()
            );
            logger.info("Added details to duplicate table");
        } catch (Exception e) {
            logger.error("Error adding to duplicate table: " + e.getMessage());
        }
    }

    // Create new user and personal details
    @Transactional
    public Long savePersonalDetails(PersonalDetailsDto personalDetailsDto) throws RuntimeException {
        logger.info("Starting new registration - checking for duplicate PAN");
        
        // Check for duplicate PAN
        if (personalDetailsDto.getPan() != null && !personalDetailsDto.getPan().trim().isEmpty()) {
            if (isPanAlreadyRegistered(personalDetailsDto.getPan()) || 
                isPanAlreadyRegisteredInPersonalDetails(personalDetailsDto.getPan(), null)) {
                logger.warn("PAN already registered: {}", personalDetailsDto.getPan());
                throw new RuntimeException("PAN already registered. Please use a different PAN or contact support.");
            }
        }
        
        // ALWAYS create a new user for registration
        MainUser user = new MainUser();
        user.setRegistrationDate(LocalDateTime.now());
        user.setStatus("IN_PROGRESS");
        user = mainUserRepository.save(user);
        logger.info("Created new user with ID: {}", user.getId());
        
        // Create new personal details
        logger.info("Creating personal details for user ID: {}", user.getId());
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setUserId(user.getId());
        updatePersonalDetailsFromDto(personalDetails, personalDetailsDto);
        
        // PAN will be automatically converted to binary by @PrePersist callback
        PersonalDetails saved = personalDetailsRepository.save(personalDetails);
        logger.info("PersonalDetails saved with binary PAN for user ID: {}", saved.getUserId());
        
        logger.info("Personal details saved and converted to binary successfully for user ID: {}", saved.getUserId());
        return saved.getUserId();
    }
    
    // Update existing user's personal details
    @Transactional
    public Long savePersonalDetails(PersonalDetailsDto personalDetailsDto, Long userId) throws RuntimeException {
        logger.info("Saving personal details for user ID: {}", userId);
        
        // Verify user exists
        MainUser user = mainUserRepository.findById(userId).orElse(null);
        if (user == null) {
            logger.error("User not found with ID: {}", userId);
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        // Check for duplicate PAN (but allow the same PAN for the same user)
        if (personalDetailsDto.getPan() != null && !personalDetailsDto.getPan().trim().isEmpty()) {
            PersonalDetails existingDetails = personalDetailsRepository.findByUserId(userId);
            if (existingDetails == null || !existingDetails.getPan().equals(personalDetailsDto.getPan())) {
                if (isPanAlreadyRegistered(personalDetailsDto.getPan()) || 
                    isPanAlreadyRegisteredInPersonalDetails(personalDetailsDto.getPan(), userId)) {
                    logger.warn("PAN already registered: {}", personalDetailsDto.getPan());
                    throw new RuntimeException("PAN already registered. Please use a different PAN or contact support.");
                }
            }
        }
        
        // Check if personal details already exist
        PersonalDetails existingDetails = personalDetailsRepository.findByUserId(userId);
        
        if (existingDetails != null) {
            // Update existing personal details
            logger.info("Updating existing personal details for user ID: {}", userId);
            updatePersonalDetailsFromDto(existingDetails, personalDetailsDto);
            PersonalDetails saved = personalDetailsRepository.save(existingDetails);
            logger.info("PersonalDetails updated with binary PAN for user ID: {}", saved.getUserId());
            return saved.getUserId();
        } else {
            // Create new personal details
            logger.info("Creating new personal details for user ID: {}", userId);
            PersonalDetails personalDetails = new PersonalDetails();
            personalDetails.setUserId(userId);
            updatePersonalDetailsFromDto(personalDetails, personalDetailsDto);
            
            // PAN will be automatically converted to binary by @PrePersist callback
            PersonalDetails saved = personalDetailsRepository.save(personalDetails);
            logger.info("PersonalDetails saved with binary PAN for user ID: {}", saved.getUserId());
            return saved.getUserId();
        }
    }

    // GET method for personal details
    public PersonalDetailsDto getPersonalDetails(Long userId) {
        PersonalDetails entity = personalDetailsRepository.findByUserId(userId);
        if (entity == null) return null;
        
        PersonalDetailsDto dto = new PersonalDetailsDto();
        dto.setTitle(entity.getTitle());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        if (entity.getDob() != null) {
            dto.setDob(entity.getDob().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        dto.setGender(entity.getGender());
        dto.setFathersFirstName(entity.getFathersFirstName());
        dto.setMothersFirstName(entity.getMothersFirstName());
        dto.setPan(entity.getPan()); // Will be converted from binary by @PostLoad
        return dto;
    }
    
    @Transactional
    public Long saveContactDetails(ContactDetailsDto contactDetailsDto, Long userId) throws RuntimeException {
        logger.info("Saving contact details for user ID: {}", userId);
        
        // Verify user exists
        MainUser user = mainUserRepository.findById(userId).orElse(null);
        if (user == null) {
            logger.error("User not found with ID: {}", userId);
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        // Validate email length
        if (contactDetailsDto.getEmail() != null && contactDetailsDto.getEmail().length() > 80) {
            throw new RuntimeException("Email address is too long. Maximum 80 characters allowed.");
        }
        
        // Validate mobile length
        if (contactDetailsDto.getMobile() != null && contactDetailsDto.getMobile().length() > 14) {
            throw new RuntimeException("Mobile number is too long. Maximum 14 digits allowed.");
        }
        
        // Check for duplicates
        Map<String, String> duplicateErrors = checkAllDuplicates(
            null, // PAN already checked in personal details
            contactDetailsDto.getEmail(),
            contactDetailsDto.getMobile(),
            null  // Account number will be checked in bank details
        );
        
        if (!duplicateErrors.isEmpty()) {
            String errorMessage = String.join(", ", duplicateErrors.values());
            throw new RuntimeException(errorMessage);
        }
        
        // Check if contact details already exist
        ContactDetails existingDetails = contactDetailsRepository.findByUserId(userId);
        
        if (existingDetails != null) {
            // Update existing details
            logger.info("Updating existing contact details for user ID: {}", userId);
            updateContactDetailsFromDto(existingDetails, contactDetailsDto);
            ContactDetails saved = contactDetailsRepository.save(existingDetails);
            logger.info("Contact details updated for user ID: {}", saved.getUserId());
            return saved.getUserId();
        } else {
            // Create new contact details
            logger.info("Creating new contact details for user ID: {}", userId);
            ContactDetails contactDetails = new ContactDetails();
            contactDetails.setUserId(userId);
            updateContactDetailsFromDto(contactDetails, contactDetailsDto);
            ContactDetails saved = contactDetailsRepository.save(contactDetails);
            logger.info("Contact details saved for user ID: {}", saved.getUserId());
            return saved.getUserId();
        }
    }

    // GET method for contact details
    public ContactDetailsDto getContactDetails(Long userId) {
        ContactDetails entity = contactDetailsRepository.findByUserId(userId);
        if (entity == null) return null;
        
        ContactDetailsDto dto = new ContactDetailsDto();
        dto.setMobile(entity.getMobile());
        dto.setEmail(entity.getEmail());
        dto.setCitizenFlag(entity.getCitizenFlag());
        dto.setPAddrLine1(entity.getPAddrLine1());
        dto.setPAddrLine2(entity.getPAddrLine2());
        dto.setPAddrLine4(entity.getPAddrLine4());
        dto.setPState(entity.getPState());
        dto.setPPincode(entity.getPPincode());
        dto.setPCountry(entity.getPCountry());
        dto.setCAddrLine1(entity.getCAddrLine1());
        dto.setCAddrLine2(entity.getCAddrLine2());
        dto.setCAddrLine4(entity.getCAddrLine4());
        dto.setCState(entity.getCState());
        dto.setCPostalCode(entity.getCPostalCode());
        dto.setCCountry(entity.getCCountry());
        return dto;
    }
    
    @Transactional
    public Long saveBankTaxDetails(BankTaxDetailsDto bankTaxDetailsDto, Long userId) throws RuntimeException {
        logger.info("Saving bank and tax details for user ID: {}", userId);
        
        // Verify user exists
        MainUser user = mainUserRepository.findById(userId).orElse(null);
        if (user == null) {
            logger.error("User not found with ID: {}", userId);
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        // Check for account number duplicate
        if (bankTaxDetailsDto.getAccountNumber() != null && !bankTaxDetailsDto.getAccountNumber().trim().isEmpty()) {
            if (isAccountNumberAlreadyRegistered(bankTaxDetailsDto.getAccountNumber())) {
                throw new RuntimeException("Bank account number already registered");
            }
        }
        
        // Check if bank tax details already exist
        BankTaxDetails existingDetails = bankTaxDetailsRepository.findByUserId(userId);
        
        if (existingDetails != null) {
            // Update existing details
            logger.info("Updating existing bank tax details for user ID: {}", userId);
            updateBankTaxDetailsFromDto(existingDetails, bankTaxDetailsDto);
            BankTaxDetails saved = bankTaxDetailsRepository.save(existingDetails);
            logger.info("Bank tax details updated for user ID: {}", saved.getUserId());
            return saved.getUserId();
        } else {
            // Create new bank tax details
            logger.info("Creating new bank tax details for user ID: {}", userId);
            BankTaxDetails bankTaxDetails = new BankTaxDetails();
            bankTaxDetails.setUserId(userId);
            updateBankTaxDetailsFromDto(bankTaxDetails, bankTaxDetailsDto);
            BankTaxDetails saved = bankTaxDetailsRepository.save(bankTaxDetails);
            logger.info("Bank tax details saved for user ID: {}", saved.getUserId());
            return saved.getUserId();
        }
    }

    // GET method for bank tax details
    public BankTaxDetailsDto getBankTaxDetails(Long userId) {
        BankTaxDetails entity = bankTaxDetailsRepository.findByUserId(userId);
        if (entity == null) return null;
        
        BankTaxDetailsDto dto = new BankTaxDetailsDto();
        dto.setAccountType(entity.getAccountType());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setIfscCode(entity.getIfscCode());
        dto.setBankName(entity.getBankName());
        dto.setIsUSPerson(entity.getIsUSPerson());
        dto.setTaxResidency(entity.getTaxResidency());
        dto.setTaxId(entity.getTaxId());
        return dto;
    }
    
    @Transactional
    public Long saveSchemeSelection(SchemeSelectionDto schemeSelectionDto, Long userId) {
        logger.info("Saving scheme selection for user ID: {}", userId);
        
        // Verify user exists
        MainUser user = mainUserRepository.findById(userId).orElse(null);
        if (user == null) {
            logger.error("User not found with ID: {}", userId);
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        // Check if scheme selection already exist
        SchemeSelection existingDetails = schemeSelectionRepository.findByUserId(userId);
        
        if (existingDetails != null) {
            // Update existing details
            logger.info("Updating existing scheme selection for user ID: {}", userId);
            updateSchemeSelectionFromDto(existingDetails, schemeSelectionDto);
            SchemeSelection saved = schemeSelectionRepository.save(existingDetails);
            logger.info("Scheme selection updated for user ID: {}", saved.getUserId());
            return saved.getUserId();
        } else {
            // Create new scheme selection
            logger.info("Creating new scheme selection for user ID: {}", userId);
            SchemeSelection schemeSelection = new SchemeSelection();
            schemeSelection.setUserId(userId);
            updateSchemeSelectionFromDto(schemeSelection, schemeSelectionDto);
            SchemeSelection saved = schemeSelectionRepository.save(schemeSelection);
            logger.info("Scheme selection saved for user ID: {}", saved.getUserId());
            return saved.getUserId();
        }
    }

    // GET method for scheme selection
    public SchemeSelectionDto getSchemeSelection(Long userId) {
        SchemeSelection entity = schemeSelectionRepository.findByUserId(userId);
        if (entity == null) return null;
        
        SchemeSelectionDto dto = new SchemeSelectionDto();
        dto.setSchemeChoice(entity.getSchemeChoice());
        dto.setLifeCycleFund(entity.getLifeCycleFund());
        return dto;
    }
    
    @Transactional
    public Long saveNomineeDetails(NomineeDetailsDto nomineeDetailsDto, Long userId) {
        logger.info("Saving nominee details for user ID: {}", userId);
        
        // Verify user exists
        MainUser user = mainUserRepository.findById(userId).orElse(null);
        if (user == null) {
            logger.error("User not found with ID: {}", userId);
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        // Delete existing nominee details
        List<NomineeDetails> existingNominees = nomineeDetailsRepository.findByUserId(userId);
        if (!existingNominees.isEmpty()) {
            nomineeDetailsRepository.deleteAll(existingNominees);
            logger.info("Deleted existing nominee details for user ID: {}", userId);
        }
        
        // Save new nominee details
        for (NomineeDetailsDto.NomineeDto nomineeDto : nomineeDetailsDto.getNominees()) {
            NomineeDetails nomineeDetails = new NomineeDetails();
            nomineeDetails.setUserId(userId);
            updateNomineeDetailsFromDto(nomineeDetails, nomineeDto);
            nomineeDetailsRepository.save(nomineeDetails);
            logger.info("Saved nominee details for nominee ID: {} of user ID: {}", nomineeDto.getId(), userId);
        }
        
        logger.info("All nominee details saved for user ID: {}", userId);
        return userId;
    }

    // GET method for nominee details
    public NomineeDetailsDto getNomineeDetails(Long userId) {
        List<NomineeDetails> entities = nomineeDetailsRepository.findByUserId(userId);
        if (entities == null || entities.isEmpty()) return null;
        
        NomineeDetailsDto dto = new NomineeDetailsDto();
        List<NomineeDetailsDto.NomineeDto> nominees = new ArrayList<>();
        
        for (NomineeDetails entity : entities) {
            NomineeDetailsDto.NomineeDto nomineeDto = new NomineeDetailsDto.NomineeDto();
            nomineeDto.setId(entity.getNomineeId());
            nomineeDto.setFirstName(entity.getFirstName());
            nomineeDto.setLastName(entity.getLastName());
            nomineeDto.setRelationship(entity.getRelationship());
            nomineeDto.setDateOfBirth(entity.getDateOfBirth());
            nomineeDto.setPercentageShare(entity.getPercentageShare());
            nomineeDto.setMinorNomineeFlag(entity.getMinorNomineeFlag());
            nomineeDto.setGuardianName(entity.getGuardianName());
            nomineeDto.setGuardianRelationship(entity.getGuardianRelationship());
            nominees.add(nomineeDto);
        }
        
        dto.setNominees(nominees);
        return dto;
    }
    
    @Transactional
    public Long saveUploadDocuments(UploadDocumentsDto uploadDocumentsDto, Long userId) {
        logger.info("Saving uploaded documents for user ID: {}", userId);
        
        // Verify user exists
        MainUser user = mainUserRepository.findById(userId).orElse(null);
        if (user == null) {
            logger.error("User not found with ID: {}", userId);
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        // Check if upload documents already exist
        UploadDocuments existingDetails = uploadDocumentsRepository.findByUserId(userId);
        
        if (existingDetails != null) {
            // Update existing details
            logger.info("Updating existing uploaded documents for user ID: {}", userId);
            updateUploadDocumentsFromDto(existingDetails, uploadDocumentsDto);
            UploadDocuments saved = uploadDocumentsRepository.save(existingDetails);
            logger.info("Uploaded documents updated for user ID: {}", saved.getUserId());
            return saved.getUserId();
        } else {
            // Create new upload documents
            logger.info("Creating new uploaded documents for user ID: {}", userId);
            UploadDocuments uploadDocuments = new UploadDocuments();
            uploadDocuments.setUserId(userId);
            updateUploadDocumentsFromDto(uploadDocuments, uploadDocumentsDto);
            UploadDocuments saved = uploadDocumentsRepository.save(uploadDocuments);
            logger.info("Uploaded documents saved for user ID: {}", saved.getUserId());
            return saved.getUserId();
        }
    }

    // GET method for upload documents
    public UploadDocumentsDto getUploadDocuments(Long userId) {
        UploadDocuments entity = uploadDocumentsRepository.findByUserId(userId);
        if (entity == null) return null;
        
        UploadDocumentsDto dto = new UploadDocumentsDto();
        if (entity.getPhotoFileData() != null) {
            dto.setPhotoFileData(Base64.getEncoder().encodeToString(entity.getPhotoFileData()));
        }
        if (entity.getSignatureFileData() != null) {
            dto.setSignatureFileData(Base64.getEncoder().encodeToString(entity.getSignatureFileData()));
        }
        dto.setSubscriberDeclaration(entity.getSubscriberDeclaration());
        return dto;
    }
    
    @Transactional
    public void completeRegistration(Long userId) {
        logger.info("Completing registration for user ID: {}", userId);
        MainUser user = mainUserRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setStatus("COMPLETED");
            mainUserRepository.save(user);
            logger.info("Registration completed for user ID: {}", userId);
        } else {
            logger.error("User not found with ID: {}", userId);
        }
    }
    
    // Add all details to duplicate table after successful registration
    @Transactional
    public void addToDuplicateAfterRegistration(Long userId) {
        try {
            // Get personal details
            PersonalDetails personalDetails = personalDetailsRepository.findByUserId(userId);
            String pan = personalDetails != null ? personalDetails.getPan() : null;
            
            // Get contact details
            ContactDetails contactDetails = contactDetailsRepository.findByUserId(userId);
            String email = contactDetails != null ? contactDetails.getEmail() : null;
            String mobile = contactDetails != null ? contactDetails.getMobile() : null;
            
            // Get bank details
            BankTaxDetails bankDetails = bankTaxDetailsRepository.findByUserId(userId);
            String accountNumber = bankDetails != null ? bankDetails.getAccountNumber() : null;
            
            // Add to duplicate table
            if (pan != null || email != null || mobile != null || accountNumber != null) {
                addToDuplicateTable(pan, email, mobile, accountNumber);
                logger.info("Added all details to duplicate table for user ID: {}", userId);
            }
        } catch (Exception e) {
            logger.error("Error adding to duplicate table after registration: " + e.getMessage());
        }
    }
    
    // Check all duplicates and return errors
    public Map<String, String> checkAllDuplicates(String pan, String email, String mobile, String accountNumber) {
        Map<String, String> errors = new HashMap<>();
        
        if (isPanAlreadyRegistered(pan)) {
            errors.put("pan", "PAN already registered");
        }
        
        if (isEmailAlreadyRegistered(email)) {
            errors.put("email", "Email already registered");
        }
        
        if (isMobileAlreadyRegistered(mobile)) {
            errors.put("mobile", "Mobile number already registered");
        }
        
        if (isAccountNumberAlreadyRegistered(accountNumber)) {
            errors.put("accountNumber", "Bank account number already registered");
        }
        
        return errors;
    }

    // GET method for user by ID
    public MainUser getUserById(Long userId) {
        return mainUserRepository.findById(userId).orElse(null);
    }
    
    // Helper methods
    private void updatePersonalDetailsFromDto(PersonalDetails personalDetails, PersonalDetailsDto dto) {
        personalDetails.setTitle(dto.getTitle());
        personalDetails.setFirstName(dto.getFirstName());
        personalDetails.setMiddleName(dto.getMiddleName());
        personalDetails.setLastName(dto.getLastName());
        
        if (dto.getDob() != null && !dto.getDob().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            personalDetails.setDob(LocalDate.parse(dto.getDob(), formatter));
        }
        
        personalDetails.setGender(dto.getGender());
        personalDetails.setFathersFirstName(dto.getFathersFirstName());
        personalDetails.setMothersFirstName(dto.getMothersFirstName());
        personalDetails.setPan(dto.getPan()); // This will be converted to binary by @PrePersist
    }
    
    private void updateContactDetailsFromDto(ContactDetails contactDetails, ContactDetailsDto dto) {
        contactDetails.setMobile(dto.getMobile() != null ? dto.getMobile().trim() : null);
        contactDetails.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : null);
        contactDetails.setCitizenFlag(dto.getCitizenFlag());
        
        // Ensure all address fields are properly set
        contactDetails.setPAddrLine1(dto.getPAddrLine1() != null ? dto.getPAddrLine1().trim() : null);
        contactDetails.setPAddrLine2(dto.getPAddrLine2() != null ? dto.getPAddrLine2().trim() : null);
        contactDetails.setPAddrLine4(dto.getPAddrLine4() != null ? dto.getPAddrLine4().trim() : null);
        contactDetails.setPState(dto.getPState());
        contactDetails.setPPincode(dto.getPPincode());
        contactDetails.setPCountry(dto.getPCountry());
        
        // Only set correspondence address if it's provided
        if (dto.getCitizenFlag() != null && !dto.getCitizenFlag().equals("RI")) {
            contactDetails.setCAddrLine1(dto.getCAddrLine1() != null ? dto.getCAddrLine1().trim() : null);
            contactDetails.setCAddrLine2(dto.getCAddrLine2() != null ? dto.getCAddrLine2().trim() : null);
            contactDetails.setCAddrLine4(dto.getCAddrLine4() != null ? dto.getCAddrLine4().trim() : null);
            contactDetails.setCState(dto.getCState() != null ? dto.getCState().trim() : null);
            contactDetails.setCPostalCode(dto.getCPostalCode() != null ? dto.getCPostalCode().trim() : null);
            contactDetails.setCCountry(dto.getCCountry() != null ? dto.getCCountry().trim() : null);
        } else {
            // Clear correspondence address fields for resident Indians
            contactDetails.setCAddrLine1(null);
            contactDetails.setCAddrLine2(null);
            contactDetails.setCAddrLine4(null);
            contactDetails.setCState(null);
            contactDetails.setCPostalCode(null);
            contactDetails.setCCountry(null);
        }
    }
    
    private void updateBankTaxDetailsFromDto(BankTaxDetails bankTaxDetails, BankTaxDetailsDto dto) {
        bankTaxDetails.setAccountType(dto.getAccountType());
        bankTaxDetails.setAccountNumber(dto.getAccountNumber());
        bankTaxDetails.setIfscCode(dto.getIfscCode());
        bankTaxDetails.setBankName(dto.getBankName());
        bankTaxDetails.setIsUSPerson(dto.getIsUSPerson());
        bankTaxDetails.setTaxResidency(dto.getTaxResidency());
        bankTaxDetails.setTaxId(dto.getTaxId());
    }
    
    private void updateSchemeSelectionFromDto(SchemeSelection schemeSelection, SchemeSelectionDto dto) {
        schemeSelection.setSchemeChoice(dto.getSchemeChoice());
        schemeSelection.setLifeCycleFund(dto.getLifeCycleFund());
    }
    
    private void updateNomineeDetailsFromDto(NomineeDetails nomineeDetails, NomineeDetailsDto.NomineeDto dto) {
        nomineeDetails.setNomineeId(dto.getId());
        nomineeDetails.setFirstName(dto.getFirstName());
        nomineeDetails.setLastName(dto.getLastName());
        nomineeDetails.setRelationship(dto.getRelationship());
        nomineeDetails.setDateOfBirth(dto.getDateOfBirth());
        nomineeDetails.setPercentageShare(dto.getPercentageShare());
        nomineeDetails.setMinorNomineeFlag(dto.getMinorNomineeFlag());
        nomineeDetails.setGuardianName(dto.getGuardianName());
        nomineeDetails.setGuardianRelationship(dto.getGuardianRelationship());
    }
    
    private void updateUploadDocumentsFromDto(UploadDocuments uploadDocuments, UploadDocumentsDto dto) {
        if (dto.getPhotoFileData() != null && !dto.getPhotoFileData().isEmpty()) {
            uploadDocuments.setPhotoFileData(Base64.getDecoder().decode(dto.getPhotoFileData()));
        }
        
        if (dto.getSignatureFileData() != null && !dto.getSignatureFileData().isEmpty()) {
            uploadDocuments.setSignatureFileData(Base64.getDecoder().decode(dto.getSignatureFileData()));
        }
        
        uploadDocuments.setSubscriberDeclaration(dto.getSubscriberDeclaration());
    }
}
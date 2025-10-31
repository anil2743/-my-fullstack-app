package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;
import java.security.SecureRandom;

@Service
public class EncryptionService {

    @Value("${encryption.key:MySecretKey12345}") // 16 bytes for AES
    private String encryptionKey;

    private static SecretKeySpec secretKey;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;

    @PostConstruct
    public void init() {
        if (encryptionKey == null || encryptionKey.length() != 16) {
            throw new IllegalStateException("AES encryption key must be exactly 16 characters long!");
        }
        secretKey = new SecretKeySpec(encryptionKey.getBytes(), "AES");
        System.out.println(">>> EncryptionService initialized successfully.");
    }

    public static String encrypt(String data) {
        if (data == null || data.trim().isEmpty()) {
            return data;
        }
        
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            
            // Generate random IV
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            
            // Combine IV and encrypted data
            byte[] combined = new byte[IV_LENGTH + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, IV_LENGTH);
            System.arraycopy(encrypted, 0, combined, IV_LENGTH, encrypted.length);
            
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting data: " + e.getMessage(), e);
        }
    }

    public static String decrypt(String encryptedData) {
        if (encryptedData == null || encryptedData.trim().isEmpty()) {
            return encryptedData;
        }
        
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedData);
            
            // Extract IV
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            // Extract encrypted data
            byte[] encrypted = new byte[combined.length - IV_LENGTH];
            System.arraycopy(combined, IV_LENGTH, encrypted, 0, encrypted.length);
            
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            
            return new String(decrypted);
        } catch (Exception e) {
            // If decryption fails, return as is (handles non-encrypted legacy data)
            System.err.println("Decryption failed, returning original value: " + e.getMessage());
            return encryptedData;
        }
    }
}
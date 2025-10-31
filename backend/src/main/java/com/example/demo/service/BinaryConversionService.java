package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class BinaryConversionService {

    private static final Logger logger = LoggerFactory.getLogger(BinaryConversionService.class);

    /**
     * Converts a string to its binary representation.
     * Each character is represented by 8 bits, separated by a space.
     * @param text The text to convert.
     * @return Binary string representation.
     */
    public static String toBinary(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }
        
        try {
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            StringBuilder binary = new StringBuilder();
            
            for (byte b : bytes) {
                // Convert byte to 8-bit binary string
                String binaryByte = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
                binary.append(binaryByte).append(' ');
            }
            
            String result = binary.toString().trim();
            logger.debug("Converted '{}' to binary (length: {})", text, result.length());
            return result;
        } catch (Exception e) {
            logger.error("Error converting text to binary: '{}'. Error: {}", text, e.getMessage(), e);
            // Return original text on failure to prevent data loss
            return text;
        }
    }

    /**
     * Converts a binary string back to text.
     * @param binary The binary string to convert (expected to have 8-bit chunks separated by spaces).
     * @return The original text.
     */
    public static String fromBinary(String binary) {
        if (binary == null || binary.trim().isEmpty()) {
            return binary;
        }
        
        // Check if the string is actually binary (contains only 0s, 1s, and spaces)
        if (!binary.matches("[01 ]+")) {
            // If not binary, return as is (handles non-binary legacy data or already-decrypted data)
            logger.debug("Input is not binary, returning as-is: '{}'", binary);
            return binary;
        }
        
        try {
            String[] binaryBytes = binary.split(" ");
            byte[] bytes = new byte[binaryBytes.length];
            
            for (int i = 0; i < binaryBytes.length; i++) {
                bytes[i] = (byte) Integer.parseInt(binaryBytes[i], 2);
            }
            
            String result = new String(bytes, StandardCharsets.UTF_8);
            logger.debug("Converted binary (length: {}) back to '{}'", binary.length(), result);
            return result;
        } catch (Exception e) {
            logger.error("Error converting binary to text. Input: '{}'. Error: {}", binary, e.getMessage(), e);
            // Return original string on failure
            return binary;
        }
    }
}
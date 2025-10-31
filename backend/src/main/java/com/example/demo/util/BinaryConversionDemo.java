package com.example.demo.util;

import com.example.demo.service.BinaryConversionService;

public class BinaryConversionDemo {
    public static void main(String[] args) {
        String originalText = "Hello World";
        String binary = BinaryConversionService.toBinary(originalText);
        String convertedBack = BinaryConversionService.fromBinary(binary);
        
        System.out.println("Original: " + originalText);
        System.out.println("Binary: " + binary);
        System.out.println("Converted back: " + convertedBack);
    }
}
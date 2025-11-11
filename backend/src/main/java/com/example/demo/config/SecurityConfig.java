package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())       // ✅ Disable CSRF
            .cors(cors -> cors.disable())       // ✅ Disable CORS here (handled in CorsConfig)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/staging/api/**").permitAll()   // ✅ STAGING API
                    .requestMatchers("/api/**").permitAll()            // ✅ PRODUCTION API
                    .anyRequest().permitAll()
            );

        return http.build();
    }
}

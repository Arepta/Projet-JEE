package com.example.edu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// This annotation indicates that the class contains Spring configuration methods.
// It is used to define beans and configuration settings for the application.
@Configuration
public class PasswordEncoderConfig {

    // This annotation marks the method as a Spring Bean, meaning the return value of this method
    // will be registered as a Spring-managed bean in the application context.
    @Bean
    public PasswordEncoder encrypt() {
        // Returns an instance of BCryptPasswordEncoder, a commonly used implementation
        // for securely hashing passwords.
        // BCrypt provides built-in salting and strong hashing algorithms, making it a secure choice.
        return new BCryptPasswordEncoder();
    }
}

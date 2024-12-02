package com.example.edu.tool;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

/*
 * Utility class for security-related functions
 */
public class Security {

    // Method to get the email of the currently logged-in user
    public static String getLoggedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}

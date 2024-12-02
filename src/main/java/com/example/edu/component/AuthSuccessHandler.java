package com.example.edu.component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// This annotation marks the class as a Spring-managed component, making it eligible for dependency injection.
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    // This method is called when authentication is successful.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
            throws IOException, ServletException {
        // Check if the authenticated user has the "ROLE_ADMIN" authority.
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            // Redirect the user to the admin dashboard if they have the "ROLE_ADMIN" role.
            response.sendRedirect("/admin/");
        } 
        // Check if the user has the "ROLE_TEACHER" authority.
        else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TEACHER"))) {
            // Redirect the user to the teacher dashboard if they have the "ROLE_TEACHER" role.
            response.sendRedirect("/teacher/");
        } 
        // Check if the user has the "ROLE_STUDENT" authority.
        else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
            // Redirect the user to the student dashboard if they have the "ROLE_STUDENT" role.
            response.sendRedirect("/student/");
        } 
        // If the user does not have any of the above roles, redirect them to the default home page.
        else {
            response.sendRedirect("/");
        }
    }
}

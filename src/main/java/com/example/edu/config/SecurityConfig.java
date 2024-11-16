package com.example.edu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.edu.component.AuthSuccessHandler;
import com.example.edu.service.AuthenticationService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final AuthenticationService customUserDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final AuthSuccessHandler AuthSuccessHandler;
	
	public SecurityConfig(AuthenticationService customUserDetailsService, PasswordEncoder passwordEncoder, AuthSuccessHandler AuthSuccessHandler) {
		this.customUserDetailsService = customUserDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.AuthSuccessHandler = AuthSuccessHandler;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests(auth -> auth
			.requestMatchers("/", "/login", "/css/**", "/js/**", "/error", "/error/**", "/WEB-INF/jsp/**", "/register").permitAll()
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.requestMatchers("/student/**").hasRole("STUDENT")
			.anyRequest().permitAll()
		)
		.formLogin(form -> form
			.loginPage("/login") // Shared login page
			.successHandler(AuthSuccessHandler) // Use the custom success handler
			.permitAll()
		)
		.logout(logout -> logout
			.logoutUrl("/logout")
			.logoutSuccessUrl("/login?logout")
			.permitAll()
		);
		
		return http.build();
	}
	
	// Define the AuthenticationManager as a bean
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	// Use a DaoAuthenticationProvider to configure UserDetailsService and PasswordEncoder
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}
}
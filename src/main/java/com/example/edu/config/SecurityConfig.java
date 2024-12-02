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
import org.springframework.web.filter.HiddenHttpMethodFilter;

import com.example.edu.component.AuthSuccessHandler;
import com.example.edu.service.AuthenticationService;

// Marks this class as a configuration class, providing Spring Beans and other configurations.
@Configuration
// Enables Spring Security and configures the security settings for the application.
@EnableWebSecurity
public class SecurityConfig {
	
	// Dependency injection for custom user details service, password encoder, and authentication success handler.
	private final AuthenticationService customUserDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final AuthSuccessHandler AuthSuccessHandler;

	// Constructor injection for the required components.
	public SecurityConfig(AuthenticationService customUserDetailsService, PasswordEncoder passwordEncoder, AuthSuccessHandler AuthSuccessHandler) {
		this.customUserDetailsService = customUserDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.AuthSuccessHandler = AuthSuccessHandler;
	}

	// Configures the security filter chain for the application.
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		// Configure authorization rules.
		.authorizeHttpRequests(auth -> auth
			// Allow unrestricted access to specific endpoints like the home page, login, static resources, and error pages.
			.requestMatchers("/", "/login", "/css/**", "/js/**", "/error", "/error/**", "/WEB-INF/jsp/**", "/register").permitAll()
			// Restrict access to paths under `/admin/**` to users with the "ADMIN" role.
			.requestMatchers("/admin/**").hasRole("ADMIN")
			// Restrict access to paths under `/teacher/**` to users with the "TEACHER" role.
			.requestMatchers("/teacher/**").hasRole("TEACHER")
			// Restrict access to paths under `/student/**` to users with the "STUDENT" role.
			.requestMatchers("/student/**").hasRole("STUDENT")
			// Permit all other requests.
			.anyRequest().permitAll()
		)
		// Configure form-based login.
		.formLogin(form -> form
			.loginPage("/login") // Specify the shared custom login page.
			.successHandler(AuthSuccessHandler) // Use the custom authentication success handler for redirection after login.
			.permitAll() // Allow access to the login page for all users.
		)
		// Configure logout settings.
		.logout(logout -> logout
			.logoutUrl("/logout") // Define the URL to trigger logout.
			.logoutSuccessUrl("/login?logout") // Redirect to the login page with a logout flag after a successful logout.
			.permitAll() // Allow everyone to access the logout URL.
		);
		
		// Return the configured security filter chain.
		return http.build();
	}

	// Expose the AuthenticationManager as a Spring Bean.
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		// Retrieve the AuthenticationManager from the provided configuration.
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	// Define a DaoAuthenticationProvider to handle authentication using a custom UserDetailsService and password encoder.
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		// Set the custom UserDetailsService implementation.
		provider.setUserDetailsService(customUserDetailsService);
		// Set the password encoder for secure password storage and verification.
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	// Define a HiddenHttpMethodFilter bean to support HTTP methods like PUT and DELETE via hidden form fields.
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
}

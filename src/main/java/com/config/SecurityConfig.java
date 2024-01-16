package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.service.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

//	@Bean
//	CorsFilter corsFilter() {
//		CorsFilter filter = new CorsFilter();
//		return filter;
//	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthFilter authFilter() {
		return new JwtAuthFilter();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/pharmacy-online/products",
						"/pharmacy-online/admin/products/**",
						"/pharmacy-online/admin/prescriptions/**",
						"/pharmacy-online/admin/category/**",
						"/pharmacy-online/admin/cart/**",
						"/pharmacy-online/admin/discount-code/**",
						"/pharmacy-online/admin/order/**",

"pharmacy-online/products/filter/rangefilt04",
						"/pharmacy-online/products/**", 
						"/pharmacy-online/products/filterByCategory", 

						"/pharmacy-online/products/filter/**", 
						"/pharmacy-online/admin/account/**",

						"/pharmacy-online/product/**",
						"/pharmacy-online/blog/**",
						"/pharmacy-online/blogs/**", 
						"/pharmacy-online/feedback/*",

						"/pharmacy-online/cart/remove-from-cart", "/pharmacy-online/cart/clear-cart",
						"/pharmacy-online/cart/update-cart", "pharmacy-online/discount-code/**",
						"/pharmacy-online/prescriptions/delete/{prescriptionId}",
						"/pharmacy-online/prescriptions/view/{prescriptionId}",
						"/pharmacy-online/prescriptions/update/{prescriptionId}",
						"/pharmacy-online/reply/byFeedbackId/{feedbackId}", "/pharmacy-online/product/feedback/**",
						"/pharmacy-online/category/*", "/un-auth/welcome", "/auth/register", "/auth/token",
						"/auth/forgot-password", "/auth/set-password").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}

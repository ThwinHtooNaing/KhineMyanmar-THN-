package com.khineMyanmar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
@EnableWebSecurity
public class SpringSecurity {

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/favicon.ico").permitAll()
                    .requestMatchers("/customer/**").permitAll()
                    .requestMatchers("/product/**").permitAll()
                    .requestMatchers("/ecom/**").permitAll()
                    .requestMatchers("/login/**").permitAll()
                    .requestMatchers("/admin/**").permitAll()
                    .requestMatchers("/delivery/**").permitAll()
                    .requestMatchers("/shopowner/**").permitAll()
                    .requestMatchers("/order/**").permitAll()
                    .requestMatchers("/signupprocess/**").permitAll()
                    .requestMatchers("/chart/**").permitAll()
                    .requestMatchers(HttpMethod.GET,"/css/**","/webfonts/**", "/js/**","/lib/**","/img/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
                    .anyRequest().authenticated()
            ) .logout(
            logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .permitAll()
        );
        return http.build();
 
    }
	 
	 @Bean
    public org.springframework.security.provisioning.InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        return new org.springframework.security.provisioning.InMemoryUserDetailsManager(
        		org.springframework.security.core.userdetails.User.withUsername("admin")
                .password(passwordEncoder.encode("admin123")) // Encrypted password
                .roles("ADMIN") // Assign ADMIN role
                .build()
        );
    }
	 
	 @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	        return authenticationConfiguration.getAuthenticationManager();
	    }
	 
}

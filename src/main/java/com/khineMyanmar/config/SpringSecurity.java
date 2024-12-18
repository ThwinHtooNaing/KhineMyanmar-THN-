package com.khineMyanmar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .authorizeHttpRequests(authorize -> authorize
                		.requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/ecom/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/delivery/**").permitAll()
                        .requestMatchers("/shopowner/**").permitAll()
                        .requestMatchers("/signupprocess/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/css/**","/webfonts/**", "/js/**","/lib/**","/img/**").permitAll()
                        .anyRequest().authenticated()
                ) .logout(
                logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
        );
        return http.build();
 
    }
}

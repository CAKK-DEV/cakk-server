package com.cakk.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import com.cakk.api.filter.JwtAuthenticationFilter;
import com.cakk.api.filter.JwtExceptionFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtExceptionFilter jwtExceptionFilter;

	@Bean
	public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
		return http
			.httpBasic(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
			.sessionManagement(setSessionManagement())
			.authorizeHttpRequests(setAuthorizePath())
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(jwtExceptionFilter, JwtAuthenticationFilter.class)
			.build();
	}

	private Customizer<SessionManagementConfigurer<HttpSecurity>> setSessionManagement() {
		return sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> setAuthorizePath() {
		return authorizeHttpRequests -> authorizeHttpRequests
			.requestMatchers("/**").permitAll()
			// .requestMatchers("/api/v1/**").hasAnyRole("USER", "MERCHANT", "ADMIN")
			.anyRequest().authenticated();
	}
}

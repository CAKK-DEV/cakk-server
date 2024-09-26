package com.cakk.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import com.cakk.api.filter.JwtAuthenticationFilter
import com.cakk.api.filter.JwtExceptionFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
	private val jwtAuthenticationFilter: JwtAuthenticationFilter,
	private val jwtExceptionFilter: JwtExceptionFilter
) {

	@Bean
	fun oauth2SecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
		return http
			.httpBasic { it.disable() }
			.csrf { it.disable() }
			.formLogin { it.disable() }
			.headers { it.frameOptions { frameOptions -> frameOptions.disable() } }
			.sessionManagement(setSessionManagement())
			.authorizeHttpRequests(setAuthorizePath())
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
			.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter::class.java)
			.build()
	}

	private fun setSessionManagement(): Customizer<SessionManagementConfigurer<HttpSecurity?>> {
		return Customizer { sessionManagementConfigurer: SessionManagementConfigurer<HttpSecurity?> ->
			sessionManagementConfigurer.sessionCreationPolicy(
				SessionCreationPolicy.STATELESS
			)
		}
	}

	private fun setAuthorizePath(): Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> {
		return Customizer {
			it
				.requestMatchers(
					"/me/**",
					"/sign-out"
				).hasAnyRole("USER", "BUSINESS_OWNER", "ADMIN")
				.requestMatchers(
					"/**"
				).permitAll()
				.anyRequest().authenticated()
		}
	}
}

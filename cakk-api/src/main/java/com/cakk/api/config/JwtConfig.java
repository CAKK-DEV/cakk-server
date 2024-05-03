package com.cakk.api.config;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtConfig {

	private final String secretKey;

	public JwtConfig(@Value("${jwt.secret}") String secretKey) {
		this.secretKey = secretKey;
	}

	@Bean
	public Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
}

package com.cakk.api.config

import java.security.Key

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig(
	@Value("\${jwt.secret}")
	private val secretKey: String
) {

	@Bean
	fun key(): Key {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
	}
}

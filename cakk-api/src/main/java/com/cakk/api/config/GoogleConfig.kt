package com.cakk.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory

@Configuration
class GoogleConfig(
	@Value("\${oauth.google.client-id}")
	private val googleClientId: String
) {

	@Bean
	fun googleIdTokenVerifier(): GoogleIdTokenVerifier {
		return GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
			.setAudience(listOf(googleClientId))
			.build()
	}
}

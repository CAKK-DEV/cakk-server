package com.cakk.api.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Configuration
public class GoogleConfig {

	@Value("${oauth.google.client-id}")
	private String googleClientId;

	@Bean
	public GoogleIdTokenVerifier googleIdTokenVerifier() {
		return new GoogleIdTokenVerifier
			.Builder(new NetHttpTransport(), new GsonFactory())
			.setAudience(Collections.singletonList(googleClientId))
			.build();
	}
}

package com.cakk.client.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.cakk.client.vo.OidcPublicKeyList;

@Component
public class AppleAuthClient {

	private final RestClient restClient;

	private final String publicKeyUrl;

	public AppleAuthClient(
		RestClient restClient,
		@Value("${oauth.apple.public-key-url}") String publicKeyUrl
	) {
		this.restClient = restClient;
		this.publicKeyUrl = publicKeyUrl;
	}

	public OidcPublicKeyList getPublicKeys() {
		return restClient.get()
			.uri(publicKeyUrl)
			.retrieve()
			.body(OidcPublicKeyList.class);
	}
}

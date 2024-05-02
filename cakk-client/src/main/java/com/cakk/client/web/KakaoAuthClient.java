package com.cakk.client.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

import com.cakk.client.vo.OidcPublicKeyList;

@Component
@RequiredArgsConstructor
public class KakaoAuthClient {

	private final RestClient restClient;

	@Value("${oauth.kakao.public-key-url}")
	private final String publicKeyUrl;

	public OidcPublicKeyList getPublicKeys() {
		return restClient.get()
			.uri(publicKeyUrl)
			.retrieve()
			.body(OidcPublicKeyList.class);
	}
}

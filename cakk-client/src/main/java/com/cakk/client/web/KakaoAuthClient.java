package com.cakk.client.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

import com.cakk.client.vo.OidcPublicKeyList;

@Component
@RequiredArgsConstructor
public class KakaoAuthClient {

	private final WebClient webClient;

	@Value("${oauth.kakao.public-key-url}")
	private final String publicKeyUrl;

	public OidcPublicKeyList getPublicKeys() {
		return webClient.mutate()
			.baseUrl(publicKeyUrl)
			.build()
			.get()
			.retrieve()
			.bodyToMono(OidcPublicKeyList.class)
			.block();
	}
}

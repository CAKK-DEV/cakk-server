package com.cakk.api.provider.oauth.impl;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.cakk.api.provider.oauth.OidcProvider;
import com.cakk.client.web.KakaoAuthClient;

@Component
@RequiredArgsConstructor
public class KakaoAuthProvider extends OidcProvider {

	private final KakaoAuthClient kakaoAuthClient;

	@Override
	public String getProviderId(String idToken) {
		return null;
	}
}

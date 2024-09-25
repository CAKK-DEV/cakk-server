package com.cakk.api.provider.oauth.impl;

import java.security.PublicKey;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.cakk.api.provider.jwt.JwtProvider;
import com.cakk.api.provider.oauth.OidcProvider;
import com.cakk.api.provider.oauth.PublicKeyProvider;
import com.cakk.external.client.KakaoAuthClient;
import com.cakk.external.vo.key.OidcPublicKeyList;

@Component
@RequiredArgsConstructor
public class KakaoAuthProvider implements OidcProvider {

	private final KakaoAuthClient kakaoAuthClient;
	private final PublicKeyProvider publicKeyProvider;
	private final JwtProvider jwtProvider;

	@Override
	public String getProviderId(final String idToken) {
		final OidcPublicKeyList oidcPublicKeyList = kakaoAuthClient.getPublicKeys();
		final PublicKey publicKey = publicKeyProvider.generatePublicKey(parseHeaders(idToken), oidcPublicKeyList);

		return jwtProvider.parseClaims(idToken, publicKey).getSubject();
	}
}

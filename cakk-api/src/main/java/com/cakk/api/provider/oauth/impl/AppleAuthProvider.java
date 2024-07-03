package com.cakk.api.provider.oauth.impl;

import java.security.PublicKey;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.cakk.api.provider.jwt.JwtProvider;
import com.cakk.api.provider.oauth.OidcProvider;
import com.cakk.api.provider.oauth.PublicKeyProvider;
import com.cakk.client.vo.OidcPublicKeyList;
import com.cakk.client.web.AppleAuthClient;

@Component
@RequiredArgsConstructor
public class AppleAuthProvider implements OidcProvider {

	private final AppleAuthClient appleAuthClient;
	private final PublicKeyProvider publicKeyProvider;
	private final JwtProvider jwtProvider;

	@Override
	public String getProviderId(final String idToken) {
		final OidcPublicKeyList oidcPublicKeyList = appleAuthClient.getPublicKeys();
		final PublicKey publicKey = publicKeyProvider.generatePublicKey(parseHeaders(idToken), oidcPublicKeyList);

		return jwtProvider.parseClaims(idToken, publicKey).getSubject();
	}
}

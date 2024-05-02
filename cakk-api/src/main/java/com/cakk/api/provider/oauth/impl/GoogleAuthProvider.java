package com.cakk.api.provider.oauth.impl;

import static com.cakk.common.enums.ReturnCode.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import lombok.RequiredArgsConstructor;

import com.cakk.api.provider.oauth.OidcProvider;
import com.cakk.common.exception.CakkException;

@Component
@RequiredArgsConstructor
public class GoogleAuthProvider extends OidcProvider {

	private final GoogleIdTokenVerifier googleIdTokenVerifier;

	@Override
	public String getProviderId(String idToken) {
		return getGoogleIdToken(idToken).getPayload().getSubject();
	}

	private GoogleIdToken getGoogleIdToken(String idToken) {
		try {
			return googleIdTokenVerifier.verify(idToken);
		} catch (GeneralSecurityException | IOException e) {
			throw new CakkException(EXTERNAL_SERVER_ERROR);
		}
	}
}

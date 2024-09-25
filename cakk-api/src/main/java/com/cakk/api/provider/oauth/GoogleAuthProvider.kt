package com.cakk.api.provider.oauth.impl;

import static com.cakk.common.enums.ReturnCode.*;
import static java.util.Objects.*;

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
public class GoogleAuthProvider implements OidcProvider {

	private final GoogleIdTokenVerifier googleIdTokenVerifier;

	@Override
	public String getProviderId(final String idToken) {
		return getGoogleIdToken(idToken).getPayload().getSubject();
	}

	private GoogleIdToken getGoogleIdToken(final String idToken) {
		try {
			final GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(idToken);

			if (isNull(googleIdToken)) {
				throw new CakkException(EXTERNAL_SERVER_ERROR);
			}

			return googleIdToken;
		} catch (GeneralSecurityException | IOException e) {
			throw new CakkException(EXTERNAL_SERVER_ERROR);
		}
	}
}

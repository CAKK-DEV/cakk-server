package com.cakk.api.provider;

import static com.cakk.api.common.utils.MockGoogleIdToken.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ProviderTest;
import com.cakk.api.provider.oauth.impl.GoogleAuthProvider;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

class GoogleAuthProviderTest extends ProviderTest {

	@InjectMocks
	private GoogleAuthProvider googleAuthProvider;

	@Mock
	private GoogleIdTokenVerifier googleIdTokenVerifier;

	private final String idToken = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9.payload.signature";

	@TestWithDisplayName("id token으로 제공자 id를 가져온다.")
	void getProviderId() throws Exception {
		// given
		String providerId = Arbitraries.strings().withCharRange('a', 'z').ofMinLength(5).ofMaxLength(10).sample();
		GoogleIdToken mockGoogleIdToken = createMockGoogleIdToken(providerId);

		doReturn(mockGoogleIdToken).when(googleIdTokenVerifier).verify(idToken);

		// when & then
		String result = googleAuthProvider.getProviderId(idToken);

		verify(googleIdTokenVerifier, times(1)).verify(idToken);
	}

	@TestWithDisplayName("google id token이 null이면 서버 외부 에러를 던진다.")
	void getProviderId2() throws Exception {
		// given
		doReturn(null).when(googleIdTokenVerifier).verify(idToken);

		// when & then
		Assertions.assertThrows(
			CakkException.class,
			() -> googleAuthProvider.getProviderId(idToken),
			ReturnCode.EXTERNAL_SERVER_ERROR.getMessage()
		);

		verify(googleIdTokenVerifier, times(1)).verify(idToken);
	}

	@TestWithDisplayName("google id token 검증 중 IOException이 발생하면 서버 외부 에러를 던진다.")
	void getProviderId3() throws Exception {
		// given
		doThrow(new IOException()).when(googleIdTokenVerifier).verify(idToken);

		// when & then
		Assertions.assertThrows(
			CakkException.class,
			() -> googleAuthProvider.getProviderId(idToken),
			ReturnCode.EXTERNAL_SERVER_ERROR.getMessage()
		);

		verify(googleIdTokenVerifier, times(1)).verify(idToken);
	}

	@TestWithDisplayName("google id token 검증 중 에러가 발생하면 서버 외부 에러를 던진다.")
	void getProviderId4() throws Exception {
		// given
		doThrow(new GeneralSecurityException()).when(googleIdTokenVerifier).verify(idToken);

		// when & then
		Assertions.assertThrows(
			CakkException.class,
			() -> googleAuthProvider.getProviderId(idToken),
			ReturnCode.EXTERNAL_SERVER_ERROR.getMessage()
		);

		verify(googleIdTokenVerifier, times(1)).verify(idToken);
	}
}

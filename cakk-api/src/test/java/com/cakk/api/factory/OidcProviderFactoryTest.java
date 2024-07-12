package com.cakk.api.factory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockitoTest;
import com.cakk.api.provider.oauth.impl.AppleAuthProvider;
import com.cakk.api.provider.oauth.impl.GoogleAuthProvider;
import com.cakk.api.provider.oauth.impl.KakaoAuthProvider;
import com.cakk.common.enums.Provider;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

public class OidcProviderFactoryTest extends MockitoTest {

	@InjectMocks
	private OidcProviderFactory oidcProviderFactory;

	@Mock
	private AppleAuthProvider appleAuthProvider;

	@Mock
	private KakaoAuthProvider kakaoAuthProvider;

	@Mock
	private GoogleAuthProvider googleAuthProvider;

	@TestWithDisplayName("애플 제공자와 idToken을 받아 제공자의 id를 반환한다")
	void getProviderId1() {
		// given
		final Provider provider = Provider.APPLE;
		final String idToken = "id";
		final String providerId = "providerId";

		doReturn(providerId).when(appleAuthProvider).getProviderId(idToken);

		// when
		String result = oidcProviderFactory.getProviderId(provider, idToken);

		// then
		assertEquals(providerId, result);

		verify(appleAuthProvider, times(1)).getProviderId(idToken);
	}

	@TestWithDisplayName("구글 제공자와 idToken을 받아 제공자의 id를 반환한다")
	void getProviderId2() {
		// given
		final Provider provider = Provider.GOOGLE;
		final String idToken = "id";
		final String providerId = "providerId";

		doReturn(providerId).when(googleAuthProvider).getProviderId(idToken);

		// when
		String result = oidcProviderFactory.getProviderId(provider, idToken);

		// then
		assertEquals(providerId, result);

		verify(googleAuthProvider, times(1)).getProviderId(idToken);
	}

	@TestWithDisplayName("카카오 제공자와 idToken을 받아 제공자의 id를 반환한다")
	void getProviderId3() {
		// given
		final Provider provider = Provider.KAKAO;
		final String idToken = "id";
		final String providerId = "providerId";

		doReturn(providerId).when(kakaoAuthProvider).getProviderId(idToken);

		// when
		String result = oidcProviderFactory.getProviderId(provider, idToken);

		// then
		assertEquals(providerId, result);

		verify(kakaoAuthProvider, times(1)).getProviderId(idToken);
	}

	@TestWithDisplayName("제공자가 null 이라면 예외를 던진다")
	void getProviderId4() {
		// given
		final Provider provider = null;
		final String idToken = "id";

		// when & then
		assertThrows(
			CakkException.class,
			() -> oidcProviderFactory.getProviderId(provider, idToken),
			ReturnCode.WRONG_PROVIDER.getMessage()
		);
	}
}

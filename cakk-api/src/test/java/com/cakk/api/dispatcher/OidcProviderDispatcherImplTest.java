package com.cakk.api.dispatcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.EnumMap;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockitoTest;
import com.cakk.api.provider.oauth.AppleAuthProvider;
import com.cakk.api.provider.oauth.GoogleAuthProvider;
import com.cakk.api.provider.oauth.KakaoAuthProvider;
import com.cakk.common.enums.Provider;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

public class OidcProviderDispatcherImplTest extends MockitoTest {

	@InjectMocks
	private OidcProviderDispatcherImpl oidcProviderDispatcherImpl;

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
		String result = oidcProviderDispatcherImpl.getProviderId(provider, idToken);

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
		String result = oidcProviderDispatcherImpl.getProviderId(provider, idToken);

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
		String result = oidcProviderDispatcherImpl.getProviderId(provider, idToken);

		// then
		assertEquals(providerId, result);

		verify(kakaoAuthProvider, times(1)).getProviderId(idToken);
	}

	@TestWithDisplayName("제공자에 해당하는 provider가 없으면 에러를 반환한다")
	void getProviderId4() {
		// given
		final Provider provider = Provider.KAKAO;
		final String idToken = "id";

		ReflectionTestUtils.setField(oidcProviderDispatcherImpl, "authProviderMap", new EnumMap<>(Provider.class));

		// when
		assertThrows(
			CakkException.class,
			() -> oidcProviderDispatcherImpl.getProviderId(provider, idToken),
			ReturnCode.WRONG_PROVIDER.getMessage()
		);
	}
}

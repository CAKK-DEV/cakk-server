package com.cakk.api.provider;

import static org.mockito.Mockito.*;

import java.security.PublicKey;

import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import io.jsonwebtoken.impl.DefaultClaims;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockitoTest;
import com.cakk.api.provider.jwt.JwtProviderImpl;
import com.cakk.api.provider.oauth.KakaoAuthProvider;
import com.cakk.api.provider.oauth.PublicKeyProvider;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.external.client.KakaoAuthClient;
import com.cakk.external.vo.key.OidcPublicKeyList;

class KakaoAuthProviderTest extends MockitoTest {

	@InjectMocks
	private KakaoAuthProvider kakaoAuthProvider;

	@Mock
	private KakaoAuthClient kakaoAuthClient;

	@Mock
	private PublicKeyProvider publicKeyProvider;

	@Mock
	private JwtProviderImpl jwtProviderImpl;

	private final String idToken = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9.payload.signature";

	@TestWithDisplayName("id token으로 제공자 id를 가져온다.")
	void getProviderId() {
		// given
		OidcPublicKeyList oidcPublicKeyList = getConstructorMonkey().giveMeBuilder(OidcPublicKeyList.class)
			.set("keys", Arbitraries.of(getOidcPublicKeyFixture()).list().ofMinSize(1).ofMaxSize(10))
			.sample();
		PublicKey publicKey = getConstructorMonkey().giveMeOne(PublicKey.class);
		DefaultClaims claims = getConstructorMonkey().giveMeBuilder(DefaultClaims.class).sample();
		claims.setSubject(Arbitraries.strings().withCharRange('a', 'z').ofMinLength(5).ofMaxLength(10).sample());

		doReturn(oidcPublicKeyList).when(kakaoAuthClient).getPublicKeys();
		doReturn(publicKey).when(publicKeyProvider).generatePublicKey(any(), any());
		doReturn(claims).when(jwtProviderImpl).parseClaims(idToken, publicKey);

		// when
		String result = kakaoAuthProvider.getProviderId(idToken);

		// then
		Assertions.assertEquals(claims.getSubject(), result);

		verify(kakaoAuthClient, times(1)).getPublicKeys();
		verify(publicKeyProvider, times(1)).generatePublicKey(any(), any());
		verify(jwtProviderImpl, times(1)).parseClaims(idToken, publicKey);
	}

	@TestWithDisplayName("공개키 목록을 가져오는 과정에서 에러가 발생하면, 서버 외부 에러를 던진다.")
	void getProviderId2() {
		// given
		doThrow(new CakkException(ReturnCode.EXTERNAL_SERVER_ERROR)).when(kakaoAuthClient).getPublicKeys();

		// when & then
		Assertions.assertThrows(
			CakkException.class,
			() -> kakaoAuthProvider.getProviderId(idToken),
			ReturnCode.EXTERNAL_SERVER_ERROR.getMessage()
		);

		verify(kakaoAuthClient, times(1)).getPublicKeys();
		verify(publicKeyProvider, times(0)).generatePublicKey(any(), any());
		verify(jwtProviderImpl, times(0)).parseClaims(any(), any());
	}

	@TestWithDisplayName("헤더 파싱 시 에러가 발생하면, 서버 내부 에러를 던진다.")
	void getProviderId3() {
		// given
		String idToken = "header.payload.signature";

		// when & then
		Assertions.assertThrows(
			CakkException.class,
			() -> kakaoAuthProvider.getProviderId(idToken),
			ReturnCode.INTERNAL_SERVER_ERROR.getMessage()
		);

		verify(kakaoAuthClient, times(1)).getPublicKeys();
		verify(publicKeyProvider, times(0)).generatePublicKey(any(), any());
		verify(jwtProviderImpl, times(0)).parseClaims(any(), any());
	}
}

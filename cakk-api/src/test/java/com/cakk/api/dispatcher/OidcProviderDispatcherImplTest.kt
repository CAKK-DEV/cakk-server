package com.cakk.api.dispatcher

import java.util.*

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.test.util.ReflectionTestUtils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.MockitoTest
import com.cakk.api.provider.oauth.AppleAuthProvider
import com.cakk.api.provider.oauth.GoogleAuthProvider
import com.cakk.api.provider.oauth.KakaoAuthProvider
import com.cakk.common.enums.Provider
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException

internal class OidcProviderDispatcherImplTest : MockitoTest() {

	@InjectMocks
	private lateinit var oidcProviderDispatcherImpl: OidcProviderDispatcherImpl

	@Mock
	private lateinit var appleAuthProvider: AppleAuthProvider

	@Mock
	private lateinit var kakaoAuthProvider: KakaoAuthProvider

	@Mock
	private lateinit var googleAuthProvider: GoogleAuthProvider

	@TestWithDisplayName("애플 제공자와 idToken을 받아 제공자의 id를 반환한다")
	fun getProviderId1() {
		// given
		val provider = Provider.APPLE
		val idToken = "id"
		val providerId = "providerId"

		doReturn(providerId).`when`(appleAuthProvider).getProviderId(idToken)

		// when
		val result = oidcProviderDispatcherImpl.getProviderId(provider, idToken)

		// then
		result shouldBe providerId

		verify(appleAuthProvider, times(1)).getProviderId(idToken)
	}

	@TestWithDisplayName("구글 제공자와 idToken을 받아 제공자의 id를 반환한다")
	fun getProviderId2() {
		// given
		val provider = Provider.GOOGLE
		val idToken = "id"
		val providerId = "providerId"

		doReturn(providerId).`when`(googleAuthProvider).getProviderId(idToken)

		// when
		val result = oidcProviderDispatcherImpl.getProviderId(provider, idToken)

		// then
		result shouldBe providerId

		verify(googleAuthProvider, times(1)).getProviderId(idToken)
	}

	@TestWithDisplayName("카카오 제공자와 idToken을 받아 제공자의 id를 반환한다")
	fun getProviderId3() {
		// given
		val provider = Provider.KAKAO
		val idToken = "id"
		val providerId = "providerId"

		doReturn(providerId).`when`(kakaoAuthProvider).getProviderId(idToken)

		// when
		val result = oidcProviderDispatcherImpl.getProviderId(provider, idToken)

		// then
		result shouldBe providerId

		verify(kakaoAuthProvider, times(1)).getProviderId(idToken)
	}

	@TestWithDisplayName("제공자에 해당하는 provider가 없으면 에러를 반환한다")
	fun getProviderId4() {
		// given
		val provider = Provider.KAKAO
		val idToken = "id"

		ReflectionTestUtils.setField(
			oidcProviderDispatcherImpl, "authProviderMap", EnumMap<Provider, Any>(
				Provider::class.java
			)
		)

		// when
		val exception = shouldThrow<CakkException> {
			oidcProviderDispatcherImpl.getProviderId(provider, idToken)
		}

		// then
		exception.getReturnCode() shouldBe ReturnCode.WRONG_PROVIDER
	}
}

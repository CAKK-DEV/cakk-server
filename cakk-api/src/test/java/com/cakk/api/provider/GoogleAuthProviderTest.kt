package com.cakk.api.provider

import java.io.IOException
import java.security.GeneralSecurityException

import org.junit.jupiter.api.Assertions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito

import net.jqwik.api.Arbitraries

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier

import org.mockito.Mockito.*

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.MockitoTest
import com.cakk.api.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.api.common.fixture.createMockGoogleIdToken
import com.cakk.api.provider.oauth.GoogleAuthProvider
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException

internal class GoogleAuthProviderTest : MockitoTest() {

	@InjectMocks
	private lateinit var googleAuthProvider: GoogleAuthProvider

	@Mock
	private lateinit var googleIdTokenVerifier: GoogleIdTokenVerifier

	private val idToken = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9.payload.signature"

	@TestWithDisplayName("id token으로 제공자 id를 가져온다.")
	fun getProviderId() {
		// given
		val providerId = getStringFixtureBw(5, 10).sample()
		val mockGoogleIdToken = createMockGoogleIdToken(providerId)

		doReturn(mockGoogleIdToken).`when`(googleIdTokenVerifier).verify(idToken)

		// when & then
		googleAuthProvider.getProviderId(idToken)

		verify(googleIdTokenVerifier, times(1)).verify(idToken)
	}

	@TestWithDisplayName("google id token이 null이면 서버 외부 에러를 던진다.")
	fun getProviderId2() {
		// given
		doReturn(null).`when`(googleIdTokenVerifier).verify(idToken)

		// when
		val exception = shouldThrow<CakkException> {
			googleAuthProvider.getProviderId(idToken)
		}

		// then
		exception.getReturnCode() shouldBe ReturnCode.EXTERNAL_SERVER_ERROR

		verify(googleIdTokenVerifier, times(1)).verify(idToken)
	}

	@TestWithDisplayName("google id token 검증 중 IOException이 발생하면 서버 외부 에러를 던진다.")
	fun getProviderId3() {
		// given
		doThrow(IOException()).`when`(googleIdTokenVerifier).verify(idToken)

		// when
		val exception = shouldThrow<CakkException> {
			googleAuthProvider.getProviderId(idToken)
		}

		// then
		exception.getReturnCode() shouldBe ReturnCode.EXTERNAL_SERVER_ERROR

		verify(googleIdTokenVerifier, times(1)).verify(idToken)
	}

	@TestWithDisplayName("google id token 검증 중 에러가 발생하면 서버 외부 에러를 던진다.")
	fun getProviderId4() {
		// given
		doThrow(GeneralSecurityException()).`when`(googleIdTokenVerifier).verify(idToken)

		// when
		val exception = shouldThrow<CakkException> {
			googleAuthProvider.getProviderId(idToken)
		}

		// then
		exception.getReturnCode() shouldBe ReturnCode.EXTERNAL_SERVER_ERROR

		verify(googleIdTokenVerifier, times(1)).verify(idToken)
	}
}

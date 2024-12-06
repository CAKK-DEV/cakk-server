package com.cakk.api.provider

import java.security.PublicKey

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.kotlin.any

import io.jsonwebtoken.impl.DefaultClaims
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import net.jqwik.api.Arbitraries

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.MockitoTest
import com.cakk.api.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.api.provider.jwt.JwtProviderImpl
import com.cakk.api.provider.oauth.AppleAuthProvider
import com.cakk.api.provider.oauth.PublicKeyProvider
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.external.client.AppleAuthClient
import com.cakk.external.vo.key.OidcPublicKey
import com.cakk.external.vo.key.OidcPublicKeyList

class AppleAuthProviderTest : MockitoTest() {

	@InjectMocks
	private lateinit var appleAuthProvider: AppleAuthProvider

	@Mock
	private lateinit var appleAuthClient: AppleAuthClient

	@Mock
	private lateinit var publicKeyProvider: PublicKeyProvider

	@Mock
	private lateinit var jwtProviderImpl: JwtProviderImpl

	private val idToken = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9.payload.signature"

	private val oidcPublicKeyFixture = fixtureMonkey.giveMeOne(OidcPublicKey::class.java)

	@TestWithDisplayName("id token으로 제공자 id를 가져온다.")
	fun getProviderId() {
		// given
		val oidcPublicKeyList = fixtureMonkey.giveMeBuilder(OidcPublicKeyList::class.java)
			.set("keys", Arbitraries.of(oidcPublicKeyFixture).list().ofMinSize(1).ofMaxSize(10))
			.sample()
		val publicKey = mock(PublicKey::class.java)
		val claims = fixtureMonkey.giveMeBuilder(DefaultClaims::class.java).sample()
		claims.setSubject(Arbitraries.strings().withCharRange('a', 'z').ofMinLength(5).ofMaxLength(10).sample())

		doReturn(oidcPublicKeyList).`when`(appleAuthClient).getPublicKeys()
		doReturn(publicKey).`when`(publicKeyProvider).generatePublicKey(any(), any())
		doReturn(claims).`when`(jwtProviderImpl).parseClaims(idToken, publicKey)

		// when
		val result = appleAuthProvider.getProviderId(idToken)

		// then
		result shouldBe claims.subject

		verify(appleAuthClient, times(1)).getPublicKeys()
		verify(publicKeyProvider, times(1)).generatePublicKey(any(), any())
		verify(jwtProviderImpl, times(1)).parseClaims(idToken, publicKey)
	}

	@TestWithDisplayName("공개키 목록을 가져오는 과정에서 에러가 발생하면, 서버 외부 에러를 던진다.")
	fun getProviderId2() {
		// given
		doThrow(CakkException(ReturnCode.EXTERNAL_SERVER_ERROR)).`when`(appleAuthClient).getPublicKeys()

		// when
		val exception = shouldThrow<CakkException> {
			appleAuthProvider.getProviderId(idToken)
		}

		// then
		exception.getReturnCode() shouldBe ReturnCode.EXTERNAL_SERVER_ERROR

		verify(appleAuthClient, times(1)).getPublicKeys()
		verify(publicKeyProvider, times(0)).generatePublicKey(any(), any())
		verify(jwtProviderImpl, times(0)).parseClaims(any(), any())
	}

	@TestWithDisplayName("헤더 파싱 시 에러가 발생하면, 서버 내부 에러를 던진다.")
	fun getProviderId3() {
		// given
		val idToken = "header.payload.signature"

		// when
		val exception = shouldThrow<CakkException> {
			appleAuthProvider.getProviderId(idToken)
		}

		// then
		exception.getReturnCode() shouldBe ReturnCode.INTERNAL_SERVER_ERROR

		verify(appleAuthClient, times(1)).getPublicKeys()
		verify(publicKeyProvider, times(0)).generatePublicKey(any(), any())
		verify(jwtProviderImpl, times(0)).parseClaims(any(), any())
	}
}

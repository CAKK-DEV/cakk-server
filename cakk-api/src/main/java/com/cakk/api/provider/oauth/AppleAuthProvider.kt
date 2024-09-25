package com.cakk.api.provider.oauth

import org.springframework.stereotype.Component

import com.cakk.api.provider.jwt.JwtProviderImpl
import com.cakk.core.provider.oauth.OidcProvider
import com.cakk.external.client.AppleAuthClient

@Component
class AppleAuthProvider(
	private val appleAuthClient: AppleAuthClient,
	private val publicKeyProvider: PublicKeyProvider,
	private val jwtProviderImpl: JwtProviderImpl
) : OidcProvider {

	override fun getProviderId(idToken: String): String {
		val oidcPublicKeyList = appleAuthClient.getPublicKeys()
		val publicKey = publicKeyProvider.generatePublicKey(parseHeaders(idToken), oidcPublicKeyList)

		return jwtProviderImpl.parseClaims(idToken, publicKey).subject
	}
}

package com.cakk.api.provider.oauth

import java.io.IOException
import java.security.GeneralSecurityException

import org.springframework.stereotype.Component

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.provider.oauth.OidcProvider

@Component
class GoogleAuthProvider(
	private val googleIdTokenVerifier: GoogleIdTokenVerifier
) : OidcProvider {

    override fun getProviderId(idToken: String): String {
        return getGoogleIdToken(idToken).payload.subject
    }

    private fun getGoogleIdToken(idToken: String): GoogleIdToken {
		return try {
			googleIdTokenVerifier.verify(idToken) ?: throw CakkException(ReturnCode.EXTERNAL_SERVER_ERROR)
		} catch (e: GeneralSecurityException) {
			throw CakkException(ReturnCode.EXTERNAL_SERVER_ERROR)
		} catch (e: IOException) {
			throw CakkException(ReturnCode.EXTERNAL_SERVER_ERROR)
		}
    }
}

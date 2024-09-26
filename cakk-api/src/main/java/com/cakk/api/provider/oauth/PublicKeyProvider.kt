package com.cakk.api.provider.oauth

import java.math.BigInteger
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.RSAPublicKeySpec

import org.springframework.stereotype.Component

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.common.utils.decodeBase64
import com.cakk.external.vo.key.OidcPublicKey
import com.cakk.external.vo.key.OidcPublicKeyList

@Component
class PublicKeyProvider {

    fun generatePublicKey(tokenHeaders: Map<String, String>, publicKeys: OidcPublicKeyList): PublicKey {
		val kid = tokenHeaders["kid"] ?: throw CakkException(ReturnCode.INTERNAL_SERVER_ERROR)
		val alg = tokenHeaders["kid"] ?: throw CakkException(ReturnCode.INTERNAL_SERVER_ERROR)
        val publicKey = publicKeys.getMatchedKey(kid, alg)

        return getPublicKey(publicKey)
    }

    private fun getPublicKey(publicKey: OidcPublicKey): PublicKey {
        val nBytes = decodeBase64(publicKey.n)
        val eBytes = decodeBase64(publicKey.e)

        val publicKeySpec = RSAPublicKeySpec(BigInteger(1, nBytes), BigInteger(1, eBytes))

		return try {
			KeyFactory.getInstance(publicKey.kty).generatePublic(publicKeySpec)
		} catch (e: NoSuchAlgorithmException) {
			throw CakkException(ReturnCode.EXTERNAL_SERVER_ERROR)
		} catch (e: InvalidKeySpecException) {
			throw CakkException(ReturnCode.EXTERNAL_SERVER_ERROR)
		}
    }
}

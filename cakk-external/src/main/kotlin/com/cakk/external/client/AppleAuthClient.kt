package com.cakk.external.client

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

import com.cakk.external.vo.key.OidcPublicKeyList

@Component
class AppleAuthClient(
	private val restClient: RestClient,
	@Value("\${oauth.apple.public-key-url}")
	private val publicKeyUrl: String
) {

    fun getPublicKeys(): OidcPublicKeyList {
		return restClient.get()
			.uri(publicKeyUrl)
			.retrieve()
			.body(OidcPublicKeyList::class.java)
			?: throw CakkException(ReturnCode.EXTERNAL_SERVER_ERROR)
	}
}

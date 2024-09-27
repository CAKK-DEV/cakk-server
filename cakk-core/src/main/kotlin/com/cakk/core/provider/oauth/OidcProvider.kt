package com.cakk.core.provider.oauth

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.common.utils.decodeBase64
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException

interface OidcProvider {
    fun getProviderId(idToken: String): String

    @Suppress("UNCHECKED_CAST")
	fun parseHeaders(token: String): Map<String, String> {
        val header = token.split(".")[0]

		return try {
			ObjectMapper().readValue(decodeBase64(header), MutableMap::class.java) as Map<String, String>
		} catch (e: IOException) {
			throw CakkException(ReturnCode.INTERNAL_SERVER_ERROR)
		}
    }
}

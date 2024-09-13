package com.cakk.external.vo.key

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException

@JvmRecord
data class OidcPublicKeyList(
	val keys: List<OidcPublicKey>
) {

	fun getMatchedKey(kid: String, alg: String): OidcPublicKey {
		return keys.find { key: OidcPublicKey ->
			key.kid == kid && key.alg == alg
		} ?: throw CakkException(ReturnCode.EXTERNAL_SERVER_ERROR)
	}
}

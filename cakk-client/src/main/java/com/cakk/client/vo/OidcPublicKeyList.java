package com.cakk.client.vo;

import static com.cakk.common.enums.ReturnCode.*;

import java.util.List;

import com.cakk.common.exception.CakkException;

public record OidcPublicKeyList(
	List<OidcPublicKey> keys
) {

	public OidcPublicKey getMatchedKey(String kid, String alg) {
		return keys.stream()
			.filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
			.findAny()
			.orElseThrow(() -> new CakkException(EXTERNAL_SERVER_ERROR));
	}
}

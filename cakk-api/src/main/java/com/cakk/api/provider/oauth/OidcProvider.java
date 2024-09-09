package com.cakk.api.provider.oauth;

import static com.cakk.common.enums.ReturnCode.*;
import static com.cakk.common.utils.DecodeUtilsKt.*;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.cakk.common.exception.CakkException;

public interface OidcProvider {

	String getProviderId(String idToken);

	default Map<String, String> parseHeaders(String token) {
		String header = token.split("\\.")[0];

		try {
			return new ObjectMapper().readValue(decodeBase64(header), Map.class);
		} catch (IOException e) {
			throw new CakkException(INTERNAL_SERVER_ERROR);
		}
	}
}

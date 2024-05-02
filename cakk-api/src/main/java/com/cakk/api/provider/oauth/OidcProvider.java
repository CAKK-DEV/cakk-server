package com.cakk.api.provider.oauth;

import static com.cakk.common.utils.DecodeUtils.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class OidcProvider {

	public abstract String getProviderId(String idToken) throws GeneralSecurityException, IOException;

	protected Map<String, String> parseHeaders(String token) throws IOException {
		String header = token.split("\\.")[0];
		return new ObjectMapper().readValue(decodeBase64(header), Map.class);
	}
}

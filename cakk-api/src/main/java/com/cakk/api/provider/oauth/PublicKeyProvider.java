package com.cakk.api.provider.oauth;

import static com.cakk.common.enums.ReturnCode.*;
import static com.cakk.common.utils.DecodeUtils.*;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cakk.client.vo.OidcPublicKey;
import com.cakk.client.vo.OidcPublicKeyList;
import com.cakk.common.exception.CakkException;

@Component
public class PublicKeyProvider {

	public PublicKey generatePublicKey(Map<String, String> tokenHeaders, OidcPublicKeyList publicKeys) {
		OidcPublicKey publicKey = publicKeys.getMatchedKey(tokenHeaders.get("kid"), tokenHeaders.get("alg"));

		return getPublicKey(publicKey);
	}

	private PublicKey getPublicKey(OidcPublicKey publicKey) {
		byte[] nBytes = decodeBase64(publicKey.n());
		byte[] eBytes = decodeBase64(publicKey.e());

		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes), new BigInteger(1, eBytes));

		try {
			return KeyFactory.getInstance(publicKey.kty()).generatePublic(publicKeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new CakkException(EXTERNAL_SERVER_ERROR);
		}
	}
}

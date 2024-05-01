package com.cakk.client.vo;

public record OidcPublicKey(
	String kid,
	String kty,
	String alg,
	String use,
	String n,
	String e
) {
}

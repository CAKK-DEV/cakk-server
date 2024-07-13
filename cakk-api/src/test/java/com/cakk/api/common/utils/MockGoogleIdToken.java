package com.cakk.api.common.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;

public class MockGoogleIdToken {

	private FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
		.build();

	public static GoogleIdToken createMockGoogleIdToken(String providerId) {
		JsonWebSignature.Header header = new JsonWebSignature.Header();
		GoogleIdToken.Payload payload = createMockPayload(providerId);

		return new GoogleIdToken(header, payload, new byte[0], new byte[0]);
	}

	public static GoogleIdToken.Payload createMockPayload(String providerId) {
		GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
		payload.setSubject(providerId);

		return payload;
	}
}

package com.cakk.api.common.fixture

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.json.webtoken.JsonWebSignature

fun createMockGoogleIdToken(providerId: String): GoogleIdToken {
	val header = JsonWebSignature.Header()
	val payload = createMockPayload(providerId)

	return GoogleIdToken(header, payload, ByteArray(0), ByteArray(0))
}

private fun createMockPayload(providerId: String?): GoogleIdToken.Payload {
	val payload = GoogleIdToken.Payload()
	payload.setSubject(providerId)

	return payload
}

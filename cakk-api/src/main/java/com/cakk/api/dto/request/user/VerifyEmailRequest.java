package com.cakk.api.dto.request.user;

public record VerifyEmailRequest(
	String email,
	String code
) {
}

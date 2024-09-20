package com.cakk.api.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequest(
	@NotBlank
	String email,
	@NotBlank
	String code
) {
}

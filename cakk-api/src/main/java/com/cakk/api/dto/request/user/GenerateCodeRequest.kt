package com.cakk.api.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record GenerateCodeRequest(
	@NotBlank
	String email
) {
}

package com.cakk.api.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.cakk.common.enums.Provider;

public record UserSignInRequest(
	@NotNull
	Provider provider,
	@NotBlank
	String idToken
) {
}

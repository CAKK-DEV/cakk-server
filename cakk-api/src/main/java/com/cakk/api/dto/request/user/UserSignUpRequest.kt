package com.cakk.api.dto.request.user;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.cakk.common.enums.Gender;
import com.cakk.common.enums.Provider;

public record UserSignUpRequest(
	@NotNull
	Provider provider,
	@NotBlank
	String idToken,
	String deviceOs,
	String deviceToken,
	@NotBlank
	String nickname,
	@NotBlank
	String email,
	@NotNull
	LocalDate birthday,
	@NotNull
	Gender gender
) {
}

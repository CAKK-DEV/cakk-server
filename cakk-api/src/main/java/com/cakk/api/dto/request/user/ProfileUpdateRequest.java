package com.cakk.api.dto.request.user;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;

import com.cakk.common.enums.Gender;

@Builder
public record ProfileUpdateRequest(
	String profileImageUrl,
	@NotEmpty
	String nickname,
	@NotEmpty
	String email,
	@NotNull
	Gender gender,
	LocalDate birthday
) {
}

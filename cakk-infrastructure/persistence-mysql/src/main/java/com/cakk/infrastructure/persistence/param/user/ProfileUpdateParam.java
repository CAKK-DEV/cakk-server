package com.cakk.infrastructure.persistence.param.user;

import java.time.LocalDate;

import lombok.Builder;

import com.cakk.common.enums.Gender;

@Builder
public record ProfileUpdateParam(
	String profileImageUrl,
	String nickname,
	String email,
	Gender gender,
	LocalDate birthday,
	Long userId
) {
}

package com.cakk.api.dto.response.user;

import java.time.LocalDate;

import lombok.Builder;

import com.cakk.common.enums.Gender;
import com.cakk.common.enums.Role;

@Builder
public record ProfileInformationResponse(
	String profileImageUrl,
	String nickname,
	String email,
	Gender gender,
	LocalDate birthday,
	Role role
) {
}

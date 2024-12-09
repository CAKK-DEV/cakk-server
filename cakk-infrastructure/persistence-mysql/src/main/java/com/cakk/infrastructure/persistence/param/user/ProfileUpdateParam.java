package com.cakk.infrastructure.persistence.param.user;

import java.time.LocalDate;

import lombok.Builder;

import com.cakk.common.enums.Gender;
import com.cakk.infrastructure.persistence.entity.user.User;

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

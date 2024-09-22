package com.cakk.domain.mysql.dto.param.user;

import java.time.LocalDate;

import lombok.Builder;

import com.cakk.common.enums.Gender;
import com.cakk.domain.mysql.entity.user.User;

@Builder
public record ProfileUpdateParam(
	String profileImageUrl,
	String nickname,
	String email,
	Gender gender,
	LocalDate birthday,
	User user
) {
}

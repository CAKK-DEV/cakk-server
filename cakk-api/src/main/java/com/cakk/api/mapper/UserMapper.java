package com.cakk.api.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.user.UserSignUpRequest;
import com.cakk.common.enums.Role;
import com.cakk.domain.mysql.entity.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

	public static User supplyUserBy(UserSignUpRequest dto, String providerId) {
		return User.builder()
			.provider(dto.provider())
			.providerId(providerId)
			.nickname(dto.nickname())
			.email(dto.email())
			.gender(dto.gender())
			.birthday(dto.birthday())
			.deviceOs(dto.deviceOs())
			.deviceToken(dto.deviceToken())
			.role(Role.USER)
			.build();
	}
}

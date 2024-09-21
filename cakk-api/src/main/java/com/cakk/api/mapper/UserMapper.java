package com.cakk.api.mapper;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.user.GenerateCodeRequest;
import com.cakk.api.dto.request.user.ProfileUpdateRequest;
import com.cakk.api.dto.request.user.UserSignUpRequest;
import com.cakk.api.dto.request.user.VerifyEmailRequest;
import com.cakk.api.dto.response.user.ProfileInformationResponse;
import com.cakk.common.enums.Role;
import com.cakk.core.dto.param.user.GenerateCodeParam;
import com.cakk.core.dto.param.user.VerifyEmailParam;
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.entity.user.UserWithdrawal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

	public static User supplyUserBy(final UserSignUpRequest dto, final String providerId) {
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

	public static ProfileInformationResponse supplyProfileInformationResponseBy(final User user) {
		return ProfileInformationResponse.builder()
			.profileImageUrl(user.getProfileImageUrl())
			.nickname(user.getNickname())
			.email(user.getEmail())
			.gender(user.getGender())
			.birthday(user.getBirthday())
			.role(user.getRole())
			.build();
	}

	public static ProfileUpdateParam supplyProfileUpdateParamBy(final ProfileUpdateRequest dto) {
		return ProfileUpdateParam.builder()
			.profileImageUrl(dto.profileImageUrl())
			.nickname(dto.nickname())
			.email(dto.email())
			.gender(dto.gender())
			.birthday(dto.birthday())
			.build();
	}

	public static UserWithdrawal supplyUserWithdrawalBy(final User user) {
		return UserWithdrawal.builder()
			.email(user.getEmail())
			.gender(user.getGender())
			.birthday(user.getBirthday())
			.role(user.getRole())
			.withdrawalDate(LocalDateTime.now())
			.build();
	}

	public static GenerateCodeParam supplyGenerateCodeParamBy(final GenerateCodeRequest dto) {
		return new GenerateCodeParam(dto.getEmail());
	}

	public static VerifyEmailParam supplyVerifyEmailParamBy(final VerifyEmailRequest dto) {
		return new VerifyEmailParam(dto.getEmail(), dto.getCode());
	}
}

package com.cakk.api.mapper;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.user.GenerateCodeRequest;
import com.cakk.api.dto.request.user.ProfileUpdateRequest;
import com.cakk.api.dto.request.user.UserSignInRequest;
import com.cakk.api.dto.request.user.UserSignUpRequest;
import com.cakk.api.dto.request.user.VerifyEmailRequest;
import com.cakk.api.dto.response.user.ProfileInformationResponse;
import com.cakk.common.enums.Role;
import com.cakk.core.dto.param.user.GenerateCodeParam;
import com.cakk.core.dto.param.user.UserSignInParam;
import com.cakk.core.dto.param.user.UserSignUpParam;
import com.cakk.core.dto.param.user.VerifyEmailParam;
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.entity.user.UserWithdrawal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

	public static User supplyUserBy(final UserSignUpParam param, final String providerId) {
		return User.builder()
			.provider(param.getProvider())
			.providerId(providerId)
			.nickname(param.getNickname())
			.email(param.getEmail())
			.gender(param.getGender())
			.birthday(param.getBirthday())
			.deviceOs(param.getDeviceOs())
			.deviceToken(param.getDeviceToken())
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

	public static ProfileUpdateParam supplyProfileUpdateParamBy(final ProfileUpdateRequest request, final User user) {
		return ProfileUpdateParam.builder()
			.profileImageUrl(request.profileImageUrl())
			.nickname(request.nickname())
			.email(request.email())
			.gender(request.gender())
			.birthday(request.birthday())
			.user(user)
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

	public static UserSignUpParam supplyUserSignUpParamBy(final UserSignUpRequest request) {
		return new UserSignUpParam(
			request.provider(),
			request.idToken(),
			request.deviceOs(),
			request.deviceToken(),
			request.nickname(),
			request.email(),
			request.birthday(),
			request.gender()
		);
	}

	public static UserSignInParam supplyUserSignInParamBy(final UserSignInRequest request) {
		return new UserSignInParam(request.provider(), request.idToken());
	}
}

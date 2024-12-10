package com.cakk.core.mapper

import java.time.LocalDateTime

import com.cakk.common.enums.Role
import com.cakk.core.dto.param.user.UserSignUpParam
import com.cakk.core.dto.response.user.JwtResponse
import com.cakk.core.dto.response.user.ProfileInformationResponse
import com.cakk.core.vo.JsonWebToken
import com.cakk.infrastructure.persistence.entity.user.UserEntity
import com.cakk.infrastructure.persistence.entity.user.UserWithdrawalEntity

fun supplyProfileInformationResponseBy(userEntity: UserEntity): ProfileInformationResponse {
	return ProfileInformationResponse(
		profileImageUrl = userEntity.profileImageUrl,
		nickname = userEntity.nickname,
		email = userEntity.email,
		gender = userEntity.gender,
		birthday = userEntity.birthday,
		role = userEntity.role
	)
}

fun supplyUserWithdrawalBy(userEntity: UserEntity): UserWithdrawalEntity {
	return UserWithdrawalEntity.builder()
		.email(userEntity.email)
		.gender(userEntity.gender)
		.birthday(userEntity.birthday)
		.role(userEntity.role)
		.withdrawalDate(LocalDateTime.now())
		.build()
}

fun supplyUserBy(param: UserSignUpParam, providerId: String): UserEntity {
	return UserEntity.builder()
		.providerType(param.providerType)
		.providerId(providerId)
		.nickname(param.nickname)
		.email(param.email)
		.gender(param.gender)
		.birthday(param.birthday)
		.deviceOs(param.deviceOs)
		.deviceToken(param.deviceToken)
		.role(Role.USER)
		.build()
}

fun supplyJwtResponseBy(dto: JsonWebToken): JwtResponse {
	return JwtResponse(
		accessToken = dto.accessToken,
		refreshToken = dto.refreshToken,
		grantType = dto.grantType
	)
}

package com.cakk.core.mapper

import java.time.LocalDateTime

import com.cakk.common.enums.Role
import com.cakk.core.dto.param.user.UserSignUpParam
import com.cakk.core.dto.response.user.JwtResponse
import com.cakk.core.dto.response.user.ProfileInformationResponse
import com.cakk.core.vo.JsonWebToken
import com.cakk.infrastructure.persistence.entity.user.User
import com.cakk.infrastructure.persistence.entity.user.UserWithdrawal

fun supplyProfileInformationResponseBy(user: com.cakk.infrastructure.persistence.entity.user.User): ProfileInformationResponse {
	return ProfileInformationResponse(
		profileImageUrl = user.profileImageUrl,
		nickname = user.nickname,
		email = user.email,
		gender = user.gender,
		birthday = user.birthday,
		role = user.role
	)
}

fun supplyUserWithdrawalBy(user: com.cakk.infrastructure.persistence.entity.user.User): com.cakk.infrastructure.persistence.entity.user.UserWithdrawal {
	return com.cakk.infrastructure.persistence.entity.user.UserWithdrawal.builder()
		.email(user.email)
		.gender(user.gender)
		.birthday(user.birthday)
		.role(user.role)
		.withdrawalDate(LocalDateTime.now())
		.build()
}

fun supplyUserBy(param: UserSignUpParam, providerId: String): com.cakk.infrastructure.persistence.entity.user.User {
	return com.cakk.infrastructure.persistence.entity.user.User.builder()
		.provider(param.provider)
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

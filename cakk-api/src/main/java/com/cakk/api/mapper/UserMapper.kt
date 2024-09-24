package com.cakk.api.mapper

import java.time.LocalDateTime

import com.cakk.api.dto.request.user.*
import com.cakk.core.dto.response.user.JwtResponse
import com.cakk.core.dto.response.user.ProfileInformationResponse
import com.cakk.api.vo.JsonWebToken
import com.cakk.common.enums.Role
import com.cakk.core.dto.param.user.GenerateCodeParam
import com.cakk.core.dto.param.user.UserSignInParam
import com.cakk.core.dto.param.user.UserSignUpParam
import com.cakk.core.dto.param.user.VerifyEmailParam
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam
import com.cakk.domain.mysql.entity.user.User
import com.cakk.domain.mysql.entity.user.UserWithdrawal

fun supplyUserBy(param: UserSignUpParam, providerId: String): User {
    return User.builder()
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


fun supplyProfileInformationResponseBy(user: User): ProfileInformationResponse {
    return ProfileInformationResponse(
        profileImageUrl = user.profileImageUrl,
        nickname = user.nickname,
        email = user.email,
        gender = user.gender,
        birthday = user.birthday,
        role = user.role
    )
}

fun supplyProfileUpdateParamBy(request: ProfileUpdateRequest, user: User): ProfileUpdateParam {
    return ProfileUpdateParam.builder()
        .profileImageUrl(request.profileImageUrl)
        .nickname(request.nickname)
        .email(request.email)
        .gender(request.gender)
        .birthday(request.birthday)
        .userId(user.id)
        .build()
}

fun supplyUserWithdrawalBy(user: User): UserWithdrawal {
    return UserWithdrawal.builder()
        .email(user.email)
        .gender(user.gender)
        .birthday(user.birthday)
        .role(user.role)
        .withdrawalDate(LocalDateTime.now())
        .build()
}

fun supplyGenerateCodeParamBy(dto: GenerateCodeRequest): GenerateCodeParam {
    return GenerateCodeParam(dto.email!!)
}

fun supplyVerifyEmailParamBy(dto: VerifyEmailRequest): VerifyEmailParam {
    return VerifyEmailParam(dto.email!!, dto.code!!)
}

fun supplyUserSignUpParamBy(request: UserSignUpRequest): UserSignUpParam {
    return UserSignUpParam(
        request.provider,
        request.idToken,
        request.deviceOs,
        request.deviceToken,
        request.nickname,
        request.email,
        request.birthday,
        request.gender
    )
}

fun supplyUserSignInParamBy(request: UserSignInRequest): UserSignInParam {
    return UserSignInParam(request.provider, request.idToken)
}

fun supplyJwtResponseBy(dto: JsonWebToken): JwtResponse {
    return JwtResponse(
        accessToken = dto.accessToken,
        refreshToken = dto.refreshToken,
        grantType = dto.grantType
    )
}

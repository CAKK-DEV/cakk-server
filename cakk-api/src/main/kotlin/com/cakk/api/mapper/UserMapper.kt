package com.cakk.api.mapper

import com.cakk.api.dto.request.user.*
import com.cakk.core.dto.param.user.GenerateCodeParam
import com.cakk.core.dto.param.user.UserSignInParam
import com.cakk.core.dto.param.user.UserSignUpParam
import com.cakk.core.dto.param.user.VerifyEmailParam
import com.cakk.infrastructure.persistence.param.user.ProfileUpdateParam
import com.cakk.infrastructure.persistence.entity.user.User

fun supplyProfileUpdateParamBy(request: ProfileUpdateRequest, user: com.cakk.infrastructure.persistence.entity.user.User): com.cakk.infrastructure.persistence.param.user.ProfileUpdateParam {
    return com.cakk.infrastructure.persistence.param.user.ProfileUpdateParam.builder()
        .profileImageUrl(request.profileImageUrl)
        .nickname(request.nickname)
        .email(request.email)
        .gender(request.gender)
        .birthday(request.birthday)
        .userId(user.id)
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
        request.provider!!,
        request.idToken!!,
        request.deviceOs,
        request.deviceToken,
        request.nickname!!,
        request.email!!,
        request.birthday!!,
        request.gender!!
    )
}

fun supplyUserSignInParamBy(request: UserSignInRequest): UserSignInParam {
    return UserSignInParam(request.provider!!, request.idToken!!)
}

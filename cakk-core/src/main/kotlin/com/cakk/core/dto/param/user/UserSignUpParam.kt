package com.cakk.core.dto.param.user

import com.cakk.common.enums.Gender
import com.cakk.common.enums.ProviderType
import java.time.LocalDate

data class UserSignUpParam(
    val providerType: ProviderType,
    val idToken: String,
    val deviceOs: String?,
    val deviceToken: String?,
    val nickname: String,
    val email: String,
    val birthday: LocalDate,
    val gender: Gender
)


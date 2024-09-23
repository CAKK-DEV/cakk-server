package com.cakk.core.dto.response.user

import java.time.LocalDate

import com.cakk.common.enums.Gender
import com.cakk.common.enums.Role

data class ProfileInformationResponse(
    val profileImageUrl: String?,
    val nickname: String,
    val email: String,
    val gender: Gender,
    val birthday: LocalDate,
    val role: Role
)

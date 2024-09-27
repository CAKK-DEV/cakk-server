package com.cakk.api.dto.request.user

import java.time.LocalDate

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

import com.cakk.common.enums.Gender

data class ProfileUpdateRequest(
    val profileImageUrl: String?,
	@field:NotEmpty
    val nickname: String?,
	@field:NotEmpty
    val email: String?,
	@field:NotNull
    val gender: Gender?,
    val birthday: LocalDate?
)

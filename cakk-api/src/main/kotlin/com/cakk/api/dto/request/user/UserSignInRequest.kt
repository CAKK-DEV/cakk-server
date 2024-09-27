package com.cakk.api.dto.request.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

import com.cakk.common.enums.Provider

data class UserSignInRequest(
	@field:NotNull
    val provider: Provider?,
	@field:NotBlank
    val idToken: String?
)

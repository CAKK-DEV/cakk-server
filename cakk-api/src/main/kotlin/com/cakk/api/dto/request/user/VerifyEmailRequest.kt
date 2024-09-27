package com.cakk.api.dto.request.user

import jakarta.validation.constraints.NotBlank

data class VerifyEmailRequest(
	@field:NotBlank
	val email: String?,
	@field:NotBlank
	val code: String?
)

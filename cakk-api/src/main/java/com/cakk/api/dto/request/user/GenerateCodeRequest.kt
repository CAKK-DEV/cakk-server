package com.cakk.api.dto.request.user

import jakarta.validation.constraints.NotBlank

data class GenerateCodeRequest(
	@field:NotBlank
    val email: String?
)

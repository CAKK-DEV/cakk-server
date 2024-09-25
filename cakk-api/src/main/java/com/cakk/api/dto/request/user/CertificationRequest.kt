package com.cakk.api.dto.request.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CertificationRequest(
	@field:NotBlank
    val businessRegistrationImageUrl: String?,
	@field:NotBlank
    val idCardImageUrl: String?,
	@field:NotNull
    val cakeShopId: Long?,
	@field:NotBlank
    val emergencyContact: String?,
    val message: String?
)

package com.cakk.api.dto.request.shop

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateShopRequest(
	@field:Size(max = 200)
	val thumbnailUrl: String?,
	@field:NotBlank @field:Size(max = 30)
	val shopName: String?,
	@field:Size(max = 40)
	val shopBio: String?,
	@field:Size(max = 500)
	val shopDescription: String?
)

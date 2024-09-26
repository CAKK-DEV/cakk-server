package com.cakk.api.dto.request.shop

import jakarta.validation.constraints.*

data class UpdateShopAddressRequest(
	@field:NotBlank @field:Size(max = 50)
	val shopAddress: String?,
	@field:NotNull @field:Min(-90) @field:Max(90)
	val latitude: Double?,
	@field:NotNull @field:Min(-180) @field:Max(180)
	val longitude: Double?
)

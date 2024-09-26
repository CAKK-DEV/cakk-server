package com.cakk.api.dto.request.shop

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

@JvmRecord
data class SearchShopByLocationRequest(
	@field:NotNull @field:Min(-90) @field:Max(90)
    val latitude: Double?,
	@field:NotNull @field:Min(-180) @field:Max(180)
    val longitude: Double?,
	@field:Min(0) @field:Max(1000)
    val distance: Double? = 1000.0
)


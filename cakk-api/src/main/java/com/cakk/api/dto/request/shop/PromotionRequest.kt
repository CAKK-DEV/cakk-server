package com.cakk.api.dto.request.shop

import jakarta.validation.constraints.NotNull

data class PromotionRequest(
	@field:NotNull
	val userId: Long?,
	@field:NotNull
	val cakeShopId: Long?
)

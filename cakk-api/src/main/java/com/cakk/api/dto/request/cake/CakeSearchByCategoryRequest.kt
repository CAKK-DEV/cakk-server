package com.cakk.api.dto.request.cake

import com.cakk.common.enums.CakeDesignCategory
import jakarta.validation.constraints.NotNull

data class CakeSearchByCategoryRequest(
	val cakeId: Long?,
	@field:NotNull
	val category:  CakeDesignCategory?,
	@field:NotNull
	val pageSize: Int = 10
)

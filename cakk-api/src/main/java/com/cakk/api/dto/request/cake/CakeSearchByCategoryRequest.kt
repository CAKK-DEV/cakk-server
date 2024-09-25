package com.cakk.api.dto.request.cake

import jakarta.validation.constraints.NotNull

import com.cakk.common.enums.CakeDesignCategory
import com.cakk.common.utils.DEFAULT_PAGE_SIZE

data class CakeSearchByCategoryRequest(
	val cakeId: Long?,
	@field:NotNull
	val category:  CakeDesignCategory?,
	val pageSize: Int = DEFAULT_PAGE_SIZE
)

package com.cakk.core.dto.param.cake

import com.cakk.common.enums.CakeDesignCategory

data class CakeSearchByCategoryParam(
	val cakeId: Long?,
	val category: CakeDesignCategory,
	val pageSize: Int
)

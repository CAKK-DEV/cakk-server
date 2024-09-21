package com.cakk.core.dto.param.cake

data class CakeSearchByShopParam(
	val cakeId: Long? = null,
	val shopId: Long,
	val pageSize: Int
)

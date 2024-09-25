package com.cakk.core.dto.response.cake

import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam

data class CakeImageListResponse(
	val cakeImages: List<CakeImageResponseParam>,
	val lastCakeId: Long?,
	val size: Int
)

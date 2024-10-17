package com.cakk.core.dto.response.cake

import com.cakk.domain.mysql.dto.param.cake.CakeImageWithShopInfoResponseParam

data class CakeImageWithShopInfoListResponse(
	val cakeImages: List<CakeImageWithShopInfoResponseParam>,
	val lastCakeId: Long?,
	val size: Int
)


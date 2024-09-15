package com.cakk.domain.mysql.dto.param.shop

data class CakeShopSearchResponseParam(
	val cakeShopId: Long,
	val thumbnailUrl: String,
	val cakeShopName: String,
	val cakeShopBio: String,
	val cakeImageUrls: Set<String>,
	val operationDays: Set<CakeShopOperationParam>
)

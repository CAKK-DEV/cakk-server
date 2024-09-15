package com.cakk.domain.mysql.dto.param.shop

data class CakeShopLocationResponseParam(
	val cakeShopId: Long,
	val thumbnailUrl: String,
	val cakeShopName: String,
	val cakeShopBio: String,
	val cakeImageUrls: Set<String>,
	val longitude: Double,
	val latitude: Double
)

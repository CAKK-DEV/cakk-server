package com.cakk.core.dto.param.shop



data class CreateShopParam(
	val businessNumber: String?,
	val operationsDays: List<ShopOperationParam>,
	val shopName: String,
	val shopBio: String?,
	val shopDescription: String?,
	val shopAddress: String,
	val latitude: Double,
	val longitude: Double,
	val links: List<ShopLinkParam>
)


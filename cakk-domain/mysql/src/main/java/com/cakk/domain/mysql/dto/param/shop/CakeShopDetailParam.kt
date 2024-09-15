package com.cakk.domain.mysql.dto.param.shop

import com.cakk.common.enums.Days

data class CakeShopDetailParam(
	val cakeShopId: Long,
	val shopName: String,
	val thumbnailUrl: String,
	val shopBio: String,
	val shopDescription: String,
	val operationDays: Set<Days>,
	val links: Set<CakeShopLinkParam>
)

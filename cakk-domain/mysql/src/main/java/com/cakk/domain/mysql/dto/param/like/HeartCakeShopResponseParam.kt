package com.cakk.domain.mysql.dto.param.like

import com.cakk.common.enums.Days

data class HeartCakeShopResponseParam(
	val cakeShopHeartId: Long,
	val cakeShopId: Long,
	val thumbnailUrl: String,
	val cakeShopName: String,
	val cakeShopBio: String,
	val cakeImageUrls: Set<String>,
	val operationDays: Set<Days>
)

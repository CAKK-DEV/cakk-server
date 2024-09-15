package com.cakk.domain.mysql.dto.param.shop

import org.locationtech.jts.geom.Point

data class CakeShopInfoParam(
	val shopAddress: String,
	val point: Point,
	val shopOperationDays: List<CakeShopOperationParam>
)

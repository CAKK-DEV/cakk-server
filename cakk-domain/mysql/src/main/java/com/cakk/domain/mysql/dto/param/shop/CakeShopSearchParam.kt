package com.cakk.domain.mysql.dto.param.shop

import org.locationtech.jts.geom.Point

data class CakeShopSearchParam(
	val cakeShopId: Long,
	val keyword: String,
	val location: Point,
	val pageSize: Int
)

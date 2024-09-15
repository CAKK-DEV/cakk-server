package com.cakk.domain.mysql.dto.param.shop

import com.cakk.domain.mysql.entity.user.User
import org.locationtech.jts.geom.Point

data class UpdateShopAddressParam(
	val shopAddress: String,
	val location: Point,
	val user: User,
	val cakeShopId: Long
)

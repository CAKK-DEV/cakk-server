package com.cakk.domain.mysql.dto.param.shop

import com.cakk.domain.mysql.entity.user.User
import lombok.Builder

data class CakeShopUpdateParam(
	val thumbnailUrl: String,
	val shopName: String,
	val shopBio: String,
	val shopDescription: String,
	val user: User,
	val cakeShopId: Long
)

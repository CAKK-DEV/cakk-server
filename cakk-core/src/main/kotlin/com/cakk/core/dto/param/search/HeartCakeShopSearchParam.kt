package com.cakk.core.dto.param.search

import com.cakk.domain.mysql.entity.user.User

data class HeartCakeShopSearchParam(
	val cakeShopHeartId: Long?,
	val pageSize: Int,
	val user: User
)

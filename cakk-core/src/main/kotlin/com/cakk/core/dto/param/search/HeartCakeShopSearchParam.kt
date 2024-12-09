package com.cakk.core.dto.param.search

import com.cakk.infrastructure.persistence.entity.user.User

data class HeartCakeShopSearchParam(
	val cakeShopHeartId: Long?,
	val pageSize: Int,
	val user: com.cakk.infrastructure.persistence.entity.user.User
)

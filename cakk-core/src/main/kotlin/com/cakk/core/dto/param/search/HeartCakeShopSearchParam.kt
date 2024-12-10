package com.cakk.core.dto.param.search

import com.cakk.infrastructure.persistence.entity.user.UserEntity

data class HeartCakeShopSearchParam(
	val cakeShopHeartId: Long?,
	val pageSize: Int,
	val userEntity: UserEntity
)

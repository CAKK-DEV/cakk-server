package com.cakk.core.dto.param.search

import com.cakk.infrastructure.persistence.entity.user.UserEntity

data class HeartCakeSearchParam(
	val cakeHeartId: Long?,
	val pageSize: Int,
	val userEntity: UserEntity
)

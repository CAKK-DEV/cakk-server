package com.cakk.core.dto.param.search

import com.cakk.infrastructure.persistence.entity.user.User

data class HeartCakeSearchParam(
	val cakeHeartId: Long?,
	val pageSize: Int,
	val user: com.cakk.infrastructure.persistence.entity.user.User
)

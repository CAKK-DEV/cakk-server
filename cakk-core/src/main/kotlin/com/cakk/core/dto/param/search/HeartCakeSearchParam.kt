package com.cakk.core.dto.param.search

import com.cakk.domain.mysql.entity.user.User

data class HeartCakeSearchParam(
	val cakeHeartId: Long?,
	val pageSize: Int,
	val user: User
)

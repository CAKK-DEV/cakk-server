package com.cakk.domain.mysql.dto.param.cake

import com.cakk.domain.mysql.entity.cake.CakeCategory
import com.cakk.domain.mysql.entity.user.User

data class CakeUpdateParam(
	val owner: User,
	val cakeId: Long,
	val cakeImageUrl: String,
	val cakeCategories: List<CakeCategory>,
	val tagNames: List<String>
)

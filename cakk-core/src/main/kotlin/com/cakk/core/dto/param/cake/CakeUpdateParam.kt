package com.cakk.core.dto.param.cake

import com.cakk.infrastructure.persistence.entity.cake.CakeCategoryEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity


data class CakeUpdateParam(
	val owner: UserEntity,
	val cakeId: Long,
	val cakeImageUrl: String,
	val cakeCategories: List<CakeCategoryEntity>,
	val tagNames: List<String>
)

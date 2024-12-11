package com.cakk.core.dto.param.cake

import com.cakk.infrastructure.persistence.entity.cake.CakeCategoryEntity
import com.cakk.infrastructure.persistence.entity.cake.CakeEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity


data class CakeCreateParam(
	val cake: CakeEntity,
	val cakeCategories: List<CakeCategoryEntity>,
	val tagNames: List<String>,
	val owner: UserEntity,
	val cakeShopId: Long
)

package com.cakk.domain.mysql.dto.param.cake

import com.cakk.common.enums.CakeDesignCategory
import com.cakk.domain.mysql.dto.param.tag.TagParam

data class CakeDetailParam(
	val cakeImageUrl: String,
	val cakeShopName: String,
	val shopBio: String,
	val cakeShopId: Long,
	val cakeCategories: Set<CakeDesignCategory>,
	val tags: Set<TagParam>
)

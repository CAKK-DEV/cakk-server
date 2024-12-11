package com.cakk.admin.mapper

import com.cakk.admin.dto.request.CakeCreateByAdminRequest
import com.cakk.admin.dto.request.CakeUpdateByAdminRequest
import com.cakk.core.dto.param.cake.CakeCreateParam
import com.cakk.core.dto.param.cake.CakeUpdateParam
import com.cakk.core.mapper.supplyCakeBy
import com.cakk.infrastructure.persistence.entity.user.UserEntity

fun supplyCakeCreateParamBy(dto: CakeCreateByAdminRequest, userEntity: UserEntity, cakeShopId: Long): CakeCreateParam {
	return CakeCreateParam(
		cake = supplyCakeBy(dto.cakeImageUrl!!),
		cakeCategories = supplyCakeCategoryListBy(dto.cakeDesignCategories!!),
		tagNames = dto.tagNames!!,
		owner = userEntity,
		cakeShopId = cakeShopId
	)
}

fun supplyCakeUpdateParamBy(dto: CakeUpdateByAdminRequest, owner: UserEntity, cakeId: Long): CakeUpdateParam {
	return CakeUpdateParam(
		owner = owner,
		cakeId = cakeId,
		cakeImageUrl = dto.cakeImageUrl!!,
		cakeCategories = supplyCakeCategoryListBy(dto.cakeDesignCategories!!),
		tagNames = dto.tagNames!!
	)
}

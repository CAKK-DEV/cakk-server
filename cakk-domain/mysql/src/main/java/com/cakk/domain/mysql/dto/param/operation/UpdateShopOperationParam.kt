package com.cakk.domain.mysql.dto.param.operation

import com.cakk.domain.mysql.entity.shop.CakeShopOperation
import com.cakk.domain.mysql.entity.user.User

data class UpdateShopOperationParam(
	val cakeShopOperations: List<CakeShopOperation>,
	val user: User,
	val cakeShopId: Long
)

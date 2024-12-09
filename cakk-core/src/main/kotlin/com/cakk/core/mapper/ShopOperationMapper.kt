package com.cakk.core.mapper

import com.cakk.core.dto.param.shop.ShopOperationParam
import com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation

fun supplyCakeShopOperationListBy(operationDays: List<ShopOperationParam>): List<com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation> {
	return operationDays.map { supplyCakeShopOperationBy(it) }.toList()
}

private fun supplyCakeShopOperationBy(param: ShopOperationParam): com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation {
	return com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation.builder()
		.operationDay(param.operationDay)
		.operationStartTime(param.operationStartTime)
		.operationEndTime(param.operationEndTime)
		.build()
}

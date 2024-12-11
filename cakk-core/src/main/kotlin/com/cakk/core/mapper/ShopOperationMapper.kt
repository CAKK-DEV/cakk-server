package com.cakk.core.mapper

import com.cakk.core.dto.param.shop.ShopOperationParam
import com.cakk.infrastructure.persistence.entity.shop.CakeShopOperationEntity

fun supplyCakeShopOperationListBy(operationDays: List<ShopOperationParam>): List<CakeShopOperationEntity> {
	return operationDays.map { supplyCakeShopOperationBy(it) }.toList()
}

private fun supplyCakeShopOperationBy(param: ShopOperationParam): CakeShopOperationEntity {
	return CakeShopOperationEntity.builder()
		.operationDay(param.operationDay)
		.operationStartTime(param.operationStartTime)
		.operationEndTime(param.operationEndTime)
		.build()
}

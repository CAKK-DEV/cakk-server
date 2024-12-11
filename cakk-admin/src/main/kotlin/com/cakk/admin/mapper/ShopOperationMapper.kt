package com.cakk.admin.mapper

import com.cakk.infrastructure.persistence.entity.shop.CakeShopOperationEntity

fun supplyCakeShopOperationListBy(operationDays: List<com.cakk.infrastructure.persistence.param.shop.ShopOperationParam>): List<CakeShopOperationEntity> {
	return operationDays
		.map { supplyCakeShopOperationBy(it) }
		.toList()
}

fun supplyCakeShopOperationBy(param: com.cakk.infrastructure.persistence.param.shop.ShopOperationParam): CakeShopOperationEntity {
	return CakeShopOperationEntity.builder()
		.operationDay(param.operationDay)
		.operationStartTime(param.operationStartTime)
		.operationEndTime(param.operationEndTime)
		.build()
}

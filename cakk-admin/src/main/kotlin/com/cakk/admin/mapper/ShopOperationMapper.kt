package com.cakk.admin.mapper

import com.cakk.infrastructure.persistence.param.shop.ShopOperationParam
import com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation

fun supplyCakeShopOperationListBy(operationDays: List<com.cakk.infrastructure.persistence.param.shop.ShopOperationParam>): List<com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation> {
	return operationDays
		.map { supplyCakeShopOperationBy(it) }
		.toList()
}

fun supplyCakeShopOperationBy(param: com.cakk.infrastructure.persistence.param.shop.ShopOperationParam): com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation {
	return com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation.builder()
		.operationDay(param.operationDay)
		.operationStartTime(param.operationStartTime)
		.operationEndTime(param.operationEndTime)
		.build()
}

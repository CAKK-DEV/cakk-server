package com.cakk.admin.mapper

import com.cakk.domain.mysql.dto.param.shop.ShopOperationParam
import com.cakk.domain.mysql.entity.shop.CakeShopOperation

fun supplyCakeShopOperationListBy(operationDays: List<ShopOperationParam>): List<CakeShopOperation> {
	return operationDays
		.map { supplyCakeShopOperationBy(it) }
		.toList()
}

fun supplyCakeShopOperationBy(param: ShopOperationParam): CakeShopOperation {
	return CakeShopOperation.builder()
		.operationDay(param.operationDay)
		.operationStartTime(param.operationStartTime)
		.operationEndTime(param.operationEndTime)
		.build()
}

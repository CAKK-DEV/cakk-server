package com.cakk.api.mapper;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.param.operation.ShopOperationParam;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopOperationMapper {

	public static List<CakeShopOperation> supplyCakeShopOperationListBy(final List<ShopOperationParam> operationDays) {
		return operationDays
			.stream()
			.map(ShopOperationMapper::supplyCakeShopOperationBy)
			.toList();
	}

	public static CakeShopOperation supplyCakeShopOperationBy(final ShopOperationParam param) {
		return CakeShopOperation.builder()
			.operationDay(param.operationDay())
			.operationStartTime(param.operationStartTime())
			.operationEndTime(param.operationEndTime())
			.build();
	}
}

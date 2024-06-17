package com.cakk.api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.operation.ShopOperationParam;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopOperationMapper {

	public static List<CakeShopOperation> supplyCakeShopOperationListBy(List<ShopOperationParam> operationDays) {
		return operationDays
			.stream()
			.map(ShopOperationMapper::supplyCakeShopOperationBy)
			.collect(Collectors.toList());
	}

	public static CakeShopOperation supplyCakeShopOperationBy(ShopOperationParam param) {
		return CakeShopOperation.builder()
			.operationDay(param.operationDay())
			.operationStartTime(param.operationStartTime())
			.operationEndTime(param.operationEndTime())
			.build();
	}
}

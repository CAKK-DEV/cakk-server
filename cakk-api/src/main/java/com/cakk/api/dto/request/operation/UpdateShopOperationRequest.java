package com.cakk.api.dto.request.operation;

import java.util.List;

import com.cakk.api.mapper.ShopOperationMapper;
import com.cakk.domain.mysql.dto.param.operation.UpdateShopOperationParam;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.entity.user.User;

import jakarta.validation.constraints.NotNull;

public record UpdateShopOperationRequest(
	@NotNull
	List<ShopOperationParam> operationDays
) {

	public UpdateShopOperationParam toParam(User user, Long cakeShopId) {
		final List<CakeShopOperation> cakeShopOperations = ShopOperationMapper.supplyCakeShopOperationListBy(operationDays);

		return new UpdateShopOperationParam(
			cakeShopOperations,
			user,
			cakeShopId
		);
	}
}

package com.cakk.api.dto.request.operation;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import com.cakk.core.dto.param.shop.ShopOperationParam;

public record UpdateShopOperationRequest(
	@NotNull
	List<ShopOperationParam> operationDays
) {
}

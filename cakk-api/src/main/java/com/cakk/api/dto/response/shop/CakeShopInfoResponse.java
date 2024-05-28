package com.cakk.api.dto.response.shop;

import java.util.List;

import com.cakk.domain.dto.param.shop.CakeShopOperationParam;

public record CakeShopInfoResponse(
	String shopAddress,
	Double latitude,
	Double longitude,
	List<CakeShopOperationParam> shopOperationDays
) {
}

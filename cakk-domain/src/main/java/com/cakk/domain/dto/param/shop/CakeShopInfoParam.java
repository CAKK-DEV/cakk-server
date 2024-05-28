package com.cakk.domain.dto.param.shop;

import java.util.List;

public record CakeShopInfoParam(
	String shopAddress,
	Double latitude,
	Double longitude,
	List<CakeShopOperationParam> shopOperationDays
) {
}

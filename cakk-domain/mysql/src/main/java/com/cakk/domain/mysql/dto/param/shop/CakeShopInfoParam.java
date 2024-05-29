package com.cakk.domain.mysql.dto.param.shop;

import java.util.List;

public record CakeShopInfoParam(
	String shopAddress,
	Double latitude,
	Double longitude,
	List<CakeShopOperationParam> shopOperationDays
) {
}

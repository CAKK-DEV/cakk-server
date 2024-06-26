package com.cakk.domain.mysql.dto.param.shop;

import java.util.List;

import org.locationtech.jts.geom.Point;

public record CakeShopInfoParam(
	String shopAddress,
	Point point,
	List<CakeShopOperationParam> shopOperationDays
) {
}

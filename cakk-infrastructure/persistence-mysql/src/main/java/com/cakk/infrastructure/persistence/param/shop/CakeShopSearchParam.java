package com.cakk.infrastructure.persistence.param.shop;

import org.locationtech.jts.geom.Point;

public record CakeShopSearchParam(
	Long cakeShopId,
	String keyword,
	Point location,
	Integer pageSize
) {
}

package com.cakk.domain.mysql.dto.param.shop;

import org.locationtech.jts.geom.Point;

import com.cakk.domain.mysql.entity.user.User;

public record UpdateShopAddressParam(
	String shopAddress,
	Point location,
	User user,
	Long cakeShopId
) {
}

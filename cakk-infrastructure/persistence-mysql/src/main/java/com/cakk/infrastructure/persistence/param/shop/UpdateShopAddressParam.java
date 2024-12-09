package com.cakk.infrastructure.persistence.param.shop;

import org.locationtech.jts.geom.Point;

import lombok.Builder;

import com.cakk.infrastructure.persistence.entity.user.User;

@Builder
public record UpdateShopAddressParam(
	String shopAddress,
	Point location,
	User user,
	Long cakeShopId
) {
}

package com.cakk.domain.mysql.dto.param.shop;

import org.locationtech.jts.geom.Point;

import lombok.Builder;

import com.cakk.domain.mysql.entity.user.User;

@Builder
public record UpdateShopAddressParam(
	String shopAddress,
	Point location,
	User user,
	Long cakeShopId
) {
}

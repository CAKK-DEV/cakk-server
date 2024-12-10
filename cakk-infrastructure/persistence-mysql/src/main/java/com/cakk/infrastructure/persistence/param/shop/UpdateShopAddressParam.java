package com.cakk.infrastructure.persistence.param.shop;

import org.locationtech.jts.geom.Point;

import lombok.Builder;

import com.cakk.infrastructure.persistence.entity.user.UserEntity;

@Builder
public record UpdateShopAddressParam(
	String shopAddress,
	Point location,
	UserEntity userEntity,
	Long cakeShopId
) {
}

package com.cakk.infrastructure.persistence.param.shop;

import lombok.Builder;

import com.cakk.infrastructure.persistence.entity.user.UserEntity;

@Builder
public record CakeShopUpdateParam(
	String thumbnailUrl,
	String shopName,
	String shopBio,
	String shopDescription,
	UserEntity user,
	Long cakeShopId
) {
}

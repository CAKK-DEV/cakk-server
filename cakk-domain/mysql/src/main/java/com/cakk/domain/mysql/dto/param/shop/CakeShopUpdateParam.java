package com.cakk.domain.mysql.dto.param.shop;

import com.cakk.domain.mysql.entity.user.User;

public record CakeShopUpdateParam(
	String thumbnailUrl,
	String shopName,
	String shopBio,
	String shopDescription,
	User user,
	Long cakeShopId
) {
}

package com.cakk.infrastructure.persistence.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.shop.CakeShop;
import com.cakk.infrastructure.persistence.entity.shop.CakeShopHeart;
import com.cakk.infrastructure.persistence.entity.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeShopHeartMapper {

	public static CakeShopHeart supplyCakeShopHeartBy(final CakeShop cakeShop, final User user) {
		return new CakeShopHeart(cakeShop, user);
	}
}

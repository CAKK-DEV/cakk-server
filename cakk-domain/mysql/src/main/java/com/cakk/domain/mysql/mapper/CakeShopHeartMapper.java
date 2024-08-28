package com.cakk.domain.mysql.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopHeart;
import com.cakk.domain.mysql.entity.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeShopHeartMapper {

	public static CakeShopHeart supplyCakeShopHeartBy(final CakeShop cakeShop, final User user) {
		return new CakeShopHeart(cakeShop, user);
	}
}

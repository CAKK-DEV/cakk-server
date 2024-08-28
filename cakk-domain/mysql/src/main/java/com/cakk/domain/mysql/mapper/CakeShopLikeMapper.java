package com.cakk.domain.mysql.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopLike;
import com.cakk.domain.mysql.entity.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeShopLikeMapper {

	public static CakeShopLike supplyCakeShopLikeBy(final CakeShop cakeShop, final User user) {
		return new CakeShopLike(cakeShop, user);
	}
}

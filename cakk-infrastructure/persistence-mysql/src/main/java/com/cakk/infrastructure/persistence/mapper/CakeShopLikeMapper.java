package com.cakk.infrastructure.persistence.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.shop.CakeShop;
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLike;
import com.cakk.infrastructure.persistence.entity.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeShopLikeMapper {

	public static CakeShopLike supplyCakeShopLikeBy(final CakeShop cakeShop, final User user) {
		return new CakeShopLike(cakeShop, user);
	}
}

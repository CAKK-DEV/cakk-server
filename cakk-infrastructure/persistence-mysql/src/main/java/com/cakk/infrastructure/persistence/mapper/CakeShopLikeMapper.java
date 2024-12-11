package com.cakk.infrastructure.persistence.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity;
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLikeEntity;
import com.cakk.infrastructure.persistence.entity.user.UserEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeShopLikeMapper {

	public static CakeShopLikeEntity supplyCakeShopLikeBy(final CakeShopEntity cakeShop, final UserEntity userEntity) {
		return new CakeShopLikeEntity(cakeShop, userEntity);
	}
}

package com.cakk.infrastructure.persistence.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity;
import com.cakk.infrastructure.persistence.entity.shop.CakeShopHeartEntity;
import com.cakk.infrastructure.persistence.entity.user.UserEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeShopHeartMapper {

	public static CakeShopHeartEntity supplyCakeShopHeartBy(final CakeShopEntity cakeShop, final UserEntity userEntity) {
		return new CakeShopHeartEntity(cakeShop, userEntity);
	}
}

package com.cakk.domain.mysql.repository.reader;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopHeart;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeShopHeartJpaRepository;

@Reader
@RequiredArgsConstructor
public class CakeShopHeartReader {

	private final CakeShopHeartJpaRepository cakeShopHeartJpaRepository;

	public CakeShopHeart findOrNullByUserAndCakeShop(final User user, final CakeShop cakeShop) {
		return cakeShopHeartJpaRepository.findByUserAndCakeShop(user, cakeShop).orElse(null);
	}
}

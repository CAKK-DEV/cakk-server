package com.cakk.domain.mysql.repository.reader;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopLike;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeShopLikeJpaRepository;

@Reader
@RequiredArgsConstructor
public class CakeShopLikeReader {

	private final CakeShopLikeJpaRepository cakeShopLikeJpaRepository;

	public CakeShopLike findOrNullByUserAndCakeShop(final User user, final CakeShop cakeShop) {
		return cakeShopLikeJpaRepository.findByUserAndCakeShop(user, cakeShop).orElse(null);
	}
}

package com.cakk.domain.mysql.repository.writer;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopLike;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeShopLikeJpaRepository;

@Writer
@RequiredArgsConstructor
public class CakeShopLikeWriter {

	private final CakeShopLikeJpaRepository cakeShopLikeJpaRepository;

	public void like(final CakeShop cakeShop, final User user) {
		final CakeShopLike cakeShopLike = new CakeShopLike(cakeShop, user);

		cakeShopLikeJpaRepository.save(cakeShopLike);
		cakeShop.increaseLikeCount();
	}
}

package com.cakk.domain.mysql.repository.writer;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;

@Writer
@RequiredArgsConstructor
public class CakeShopLikeWriter {

	public void like(final CakeShop cakeShop, final User user) {
		user.likeCakeShop(cakeShop);
	}
}

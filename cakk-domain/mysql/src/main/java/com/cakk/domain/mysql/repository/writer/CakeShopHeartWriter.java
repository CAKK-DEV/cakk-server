package com.cakk.domain.mysql.repository.writer;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.shop.CakeShopHeart;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeShopHeartJpaRepository;

@Writer
@RequiredArgsConstructor
public class CakeShopHeartWriter {

	private final CakeShopHeartJpaRepository cakeShopHeartJpaRepository;

	public void deleteAllByUser(final User user) {
		final List<CakeShopHeart> cakeShopHearts = cakeShopHeartJpaRepository.findAllByUser(user);

		if (cakeShopHearts == null || cakeShopHearts.isEmpty()) {
			return;
		}

		cakeShopHeartJpaRepository.deleteAllInBatch(cakeShopHearts);
	}
}

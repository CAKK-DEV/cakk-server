package com.cakk.domain.mysql.repository.writer;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.shop.CakeShopLike;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeShopLikeJpaRepository;

@Writer
@RequiredArgsConstructor
public class CakeShopLikeWriter {

	private final CakeShopLikeJpaRepository cakeShopLikeJpaRepository;

	public void deleteAllByUser(final User user) {
		final List<CakeShopLike> cakeShopLikes = cakeShopLikeJpaRepository.findAllByUser(user);

		if (cakeShopLikes == null || cakeShopLikes.isEmpty()) {
			return;
		}

		cakeShopLikeJpaRepository.deleteAllInBatch(cakeShopLikes);
	}
}

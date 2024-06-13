package com.cakk.domain.mysql.repository.writer;

import static java.util.Objects.*;

import java.util.List;

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

	public void likeOrCancel(final CakeShopLike cakeShopLike, final User user, final CakeShop cakeShop) {
		if (isNull(cakeShopLike)) {
			this.like(cakeShop, user);
		} else {
			this.cancelLike(cakeShopLike);
		}
	}

	public void deleteAllByUser(final User user) {
		final List<CakeShopLike> cakeShopLikes = cakeShopLikeJpaRepository.findAllByUser(user);

		if (cakeShopLikes == null || cakeShopLikes.isEmpty()) {
			return;
		}

		cakeShopLikeJpaRepository.deleteAllInBatch(cakeShopLikes);
	}

	private void like(final CakeShop cakeShop, final User user) {
		final CakeShopLike cakeShopLike = new CakeShopLike(cakeShop, user);

		cakeShopLikeJpaRepository.save(cakeShopLike);
		cakeShop.increaseLikeCount();
	}

	private void cancelLike(final CakeShopLike cakeShopLike) {
		final CakeShop cakeShop = cakeShopLike.getCakeShop();

		cakeShopLikeJpaRepository.delete(cakeShopLike);
		cakeShop.decreaseLikeCount();
	}
}

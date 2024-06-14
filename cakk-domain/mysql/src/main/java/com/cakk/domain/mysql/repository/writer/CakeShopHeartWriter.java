package com.cakk.domain.mysql.repository.writer;

import static java.util.Objects.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopHeart;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeShopHeartJpaRepository;

@Writer
@RequiredArgsConstructor
public class CakeShopHeartWriter {

	private final CakeShopHeartJpaRepository cakeShopHeartJpaRepository;

	public void heartOrCancel(final CakeShopHeart cakeShopHeart, final User user, final CakeShop cakeShop) {
		if (isNull(cakeShopHeart)) {
			this.heart(cakeShop, user);
		} else {
			this.cancelHeart(cakeShopHeart);
		}
	}

	public void deleteAllByUser(final User user) {
		final List<CakeShopHeart> cakeShopHearts = cakeShopHeartJpaRepository.findAllByUser(user);

		if (cakeShopHearts == null || cakeShopHearts.isEmpty()) {
			return;
		}

		cakeShopHeartJpaRepository.deleteAllInBatch(cakeShopHearts);
	}

	private void heart(final CakeShop cakeShop, final User user) {
		final CakeShopHeart cakeShopHeart = new CakeShopHeart(cakeShop, user);

		cakeShopHeartJpaRepository.save(cakeShopHeart);
		cakeShop.increaseHeartCount();
	}

	private void cancelHeart(final CakeShopHeart cakeShopHeart) {
		final CakeShop cakeShop = cakeShopHeart.getCakeShop();

		cakeShopHeartJpaRepository.delete(cakeShopHeart);
		cakeShop.decreaseHeartCount();
	}
}

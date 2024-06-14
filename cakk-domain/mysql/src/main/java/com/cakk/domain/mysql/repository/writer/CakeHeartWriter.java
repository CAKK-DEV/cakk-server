package com.cakk.domain.mysql.repository.writer;

import static java.util.Objects.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeHeart;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeHeartJpaRepository;

@Writer
@RequiredArgsConstructor
public class CakeHeartWriter {

	private final CakeHeartJpaRepository cakeHeartJpaRepository;

	public void heartOrCancel(final CakeHeart cakeHeart, final User user, final Cake cake) {
		if (isNull(cakeHeart)) {
			this.heart(cake, user);
		} else {
			this.cancelHeart(cakeHeart);
		}
	}

	public void deleteAllByUser(final User user) {
		final List<CakeHeart> cakeHearts = cakeHeartJpaRepository.findAllByUser(user);

		if (isNull(cakeHearts) || cakeHearts.isEmpty()) {
			return;
		}

		cakeHeartJpaRepository.deleteAllInBatch(cakeHearts);
	}

	private void heart(final Cake cake, final User user) {
		final CakeHeart cakeHeart = new CakeHeart(cake, user);

		cakeHeartJpaRepository.save(cakeHeart);
		cake.increaseHeartCount();
	}

	private void cancelHeart(final CakeHeart cakeHeart) {
		final Cake cake = cakeHeart.getCake();

		cakeHeartJpaRepository.delete(cakeHeart);
		cake.decreaseHeartCount();
	}
}

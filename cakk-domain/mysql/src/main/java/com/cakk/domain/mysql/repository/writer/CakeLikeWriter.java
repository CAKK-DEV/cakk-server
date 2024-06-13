package com.cakk.domain.mysql.repository.writer;

import static java.util.Objects.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeLike;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeLikeJpaRepository;

@Writer
@RequiredArgsConstructor
public class CakeLikeWriter {

	private final CakeLikeJpaRepository cakeLikeJpaRepository;

	public void likeOrCancel(final CakeLike cakeLike, final User user, final Cake cake) {
		if (isNull(cakeLike)) {
			this.like(cake, user);
		} else {
			this.cancelLike(cakeLike);
		}
	}

	public void deleteAllByUser(final User user) {
		final List<CakeLike> cakeLikes = cakeLikeJpaRepository.findAllByUser(user);

		if (isNull(cakeLikes) || cakeLikes.isEmpty()) {
			return;
		}

		cakeLikeJpaRepository.deleteAllInBatch(cakeLikes);
	}

	private void like(final Cake cake, final User user) {
		final CakeLike cakeLike = new CakeLike(cake, user);

		cakeLikeJpaRepository.save(cakeLike);
		cake.increaseLikeCount();
	}

	private void cancelLike(final CakeLike cakeLike) {
		final Cake cake = cakeLike.getCake();

		cakeLikeJpaRepository.delete(cakeLike);
		cake.decreaseLikeCount();
	}
}

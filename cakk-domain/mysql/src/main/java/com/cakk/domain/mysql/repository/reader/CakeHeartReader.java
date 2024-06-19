package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeHeart;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeHeartJpaRepository;
import com.cakk.domain.mysql.repository.query.CakeHeartQueryRepository;

@RequiredArgsConstructor
@Reader
public class CakeHeartReader {

	private final CakeHeartJpaRepository cakeHeartJpaRepository;
	private final CakeHeartQueryRepository cakeHeartQueryRepository;

	public List<HeartCakeImageResponseParam> searchCakeImagesByCursorAndHeart(
		final Long cakeHeartId,
		final Long userId,
		final int pageSize
	) {
		return cakeHeartQueryRepository.searchCakeImagesByCursorAndHeart(cakeHeartId, userId, pageSize);
	}

	public CakeHeart findOrNullByUserAndCake(final User user, final Cake cake) {
		return cakeHeartJpaRepository.findByUserAndCake(user, cake).orElse(null);
	}

	public boolean existsByUserAndCake(final User user, final Cake cake) {
		return cakeHeartJpaRepository.existsByUserAndCake(user, cake);
	}
}

package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;
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
}

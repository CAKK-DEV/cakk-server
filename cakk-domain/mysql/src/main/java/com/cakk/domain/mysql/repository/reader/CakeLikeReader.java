package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.dto.param.like.LikeCakeImageResponseParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeLike;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeLikeJpaRepository;
import com.cakk.domain.mysql.repository.query.CakeLikeQueryRepository;

@RequiredArgsConstructor
@Reader
public class CakeLikeReader {

	private final CakeLikeJpaRepository cakeLikeJpaRepository;
	private final CakeLikeQueryRepository cakeLikeQueryRepository;

	public List<LikeCakeImageResponseParam> searchCakeImagesByCursorAndLike(final Long cakeLikeId, final Long userId, final int pageSize) {
		return cakeLikeQueryRepository.searchCakeImagesByCursorAndLike(cakeLikeId, userId, pageSize);
	}

	public CakeLike findOrNullByUserAndCake(final User user, final Cake cake) {
		return cakeLikeJpaRepository.findByUserAndCake(user, cake).orElse(null);
	}
}

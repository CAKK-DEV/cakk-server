package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.dto.param.like.HeartCakeShopResponseParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopHeart;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeShopHeartJpaRepository;
import com.cakk.domain.mysql.repository.query.CakeShopHeartQueryRepository;

@Reader
@RequiredArgsConstructor
public class CakeShopHeartReader {

	private final CakeShopHeartQueryRepository cakeShopHeartQueryRepository;
	private final CakeShopHeartJpaRepository cakeShopHeartJpaRepository;

	public List<HeartCakeShopResponseParam> searchAllByCursorAndHeart(
		final Long cakeShopHeartId,
		final Long userId,
		final int pageSize
	) {
		final List<Long> cakeShopHeartIds = cakeShopHeartQueryRepository.searchIdsByCursorAndHeart(cakeShopHeartId, userId, pageSize);

		return cakeShopHeartQueryRepository.searchAllByCursorAndHeart(cakeShopHeartIds);
	}

	public CakeShopHeart findOrNullByUserAndCakeShop(final User user, final CakeShop cakeShop) {
		return cakeShopHeartJpaRepository.findByUserAndCakeShop(user, cakeShop).orElse(null);
	}

	public boolean existsByUserAndCakeShop(final User user, final CakeShop cakeShop) {
		return cakeShopHeartJpaRepository.existsByUserAndCakeShop(user, cakeShop);
	}
}

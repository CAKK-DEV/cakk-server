package com.cakk.api.service.like;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.DistributedLock;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.CakeShopLikeReader;
import com.cakk.domain.mysql.repository.reader.CakeShopReader;
import com.cakk.domain.mysql.repository.writer.CakeShopLikeWriter;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final CakeShopReader cakeShopReader;
	private final CakeShopLikeReader cakeShopLikeReader;
	private final CakeShopLikeWriter cakeShopLikeWriter;

	@Transactional(readOnly = true)
	public void validateLikeCount(final User user, final Long cakeShopId) {
		final int likeCount = cakeShopLikeReader.countByCakeShopIdAndUser(cakeShopId, user);

		if (likeCount >= 50) {
			throw new CakkException(ReturnCode.MAX_CAKE_SHOP_LIKE);
		}
	}

	@DistributedLock(key = "#cakeShopId")
	public void likeCakeShop(final User user, final Long cakeShopId) {
		final CakeShop cakeShop = cakeShopReader.findById(cakeShopId);

		cakeShopLikeWriter.like(cakeShop, user);
	}
}

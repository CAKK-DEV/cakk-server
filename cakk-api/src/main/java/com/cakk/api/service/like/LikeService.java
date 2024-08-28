package com.cakk.api.service.like;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.DistributedLock;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.facade.user.UserLikeFacade;
import com.cakk.domain.mysql.repository.reader.CakeShopReader;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final CakeShopReader cakeShopReader;
	private final UserLikeFacade userCakeFacade;

	@DistributedLock(key = "#cakeShopId")
	public void likeCakeShop(final User user, final Long cakeShopId) {
		final CakeShop cakeShop = cakeShopReader.findByIdWithLike(cakeShopId);

		userCakeFacade.likeCakeShop(user, cakeShop);
	}
}

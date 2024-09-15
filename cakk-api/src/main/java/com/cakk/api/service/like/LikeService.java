package com.cakk.api.service.like;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.DistributedLock;
import com.cakk.core.facade.cake.CakeShopReadFacade;
import com.cakk.core.facade.user.UserLikeFacade;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final CakeShopReadFacade cakeShopReadFacade;
	private final UserLikeFacade userCakeFacade;

	@DistributedLock(key = "#cakeShopId")
	public void likeCakeShop(final User user, final Long cakeShopId) {
		final CakeShop cakeShop = cakeShopReadFacade.findByIdWithLike(cakeShopId);

		userCakeFacade.likeCakeShop(user, cakeShop);
	}
}

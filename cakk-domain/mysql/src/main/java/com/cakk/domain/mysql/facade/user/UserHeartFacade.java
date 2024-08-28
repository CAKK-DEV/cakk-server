package com.cakk.domain.mysql.facade.user;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.DomainFacade;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;

@RequiredArgsConstructor
@DomainFacade
public class UserHeartFacade {

	public void heartCake(final User user, final Cake cake) {
		if (!cake.isHeartedBy(user)) {
			user.heartCake(cake);
		} else {
			user.unHeartCake(cake);
		}
	}

	public void heartCakeShop(final User user, final CakeShop cakeShop) {
		if (!cakeShop.isHeartedBy(user)) {
			user.heartCakeShop(cakeShop);
		} else {
			user.unHeartCakeShop(cakeShop);
		}
	}
}

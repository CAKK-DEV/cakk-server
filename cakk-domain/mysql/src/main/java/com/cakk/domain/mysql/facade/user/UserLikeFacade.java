package com.cakk.domain.mysql.facade.user;

import com.cakk.domain.mysql.annotation.DomainFacade;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;

@DomainFacade
public class UserLikeFacade {

	public void likeCakeShop(final User user, final CakeShop cakeShop) {
		user.likeCakeShop(cakeShop);
	}
}

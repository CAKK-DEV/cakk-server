package com.cakk.domain.facade.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.enums.Role;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.base.FacadeTest;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.facade.user.UserLikeFacade;

class UserLikeFacadeTest extends FacadeTest {

	@InjectMocks
	private UserLikeFacade userLikeFacade;

	@Test
	@DisplayName("케이크 샵 기대돼요 동작을 성공한다")
	void likeCakeShop() {
		//given
		final User user = getUserFixture(Role.USER);
		final CakeShop cakeShop = getCakeShopFixture();

		//when
		userLikeFacade.likeCakeShop(user, cakeShop);

		//then
		assertThat(cakeShop.getLikeCount()).isEqualTo(1);
		final boolean liked = cakeShop.getShopLikes().stream().anyMatch(it -> it.getUser().equals(user));
		assertTrue(liked);
	}

	@Test
	@DisplayName("케이크 샵 기대돼요 동작이 50회 초과로 실패한다")
	void heartCakeShop2() {
		// given
		final User user = getUserFixture(Role.USER);
		final CakeShop cakeShop = getCakeShopFixture();

		IntStream.range(0, 50).forEach(i -> userLikeFacade.likeCakeShop(user, cakeShop));

		// when & then
		assertThrows(
			CakkException.class,
			() -> userLikeFacade.likeCakeShop(user, cakeShop),
			ReturnCode.MAX_CAKE_SHOP_LIKE.getMessage()
		);
	}
}

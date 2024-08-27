package com.cakk.domain.facade.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cakk.common.enums.Role;
import com.cakk.domain.base.DomainTest;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.facade.user.UserHeartFacade;

class UserHeartFacadeTest extends DomainTest {

	private final UserHeartFacade userHeartFacade = new UserHeartFacade();

	@Test
	@DisplayName("케이크 하트를 성공한다")
	void heartCake() {
		//given
		final User user = getUserFixture(Role.USER);
		final Cake cake = getCakeFixture();

		//when
		userHeartFacade.heartCake(user, cake);

		//then
		assertThat(cake.isHeartedBy(user)).isTrue();
	}

	@Test
	@DisplayName("케이크 하트 취소를 성공한다")
	void heartCake2() {
		//given
		final User user = getUserFixture(Role.USER);
		final Cake cake = getCakeFixture();
		userHeartFacade.heartCake(user, cake);

		//when
		userHeartFacade.heartCake(user, cake);

		//then
		assertThat(cake.isHeartedBy(user)).isFalse();
	}

	@Test
	@DisplayName("케이크 샵 하트를 성공한다")
	void heartCakeShop() {
		//given
		final User user = getUserFixture(Role.USER);
		final CakeShop cakeShop = getCakeShopFixture();

		//when
		userHeartFacade.heartCakeShop(user, cakeShop);

		//then
		assertThat(cakeShop.isHeartedBy(user)).isTrue();
	}

	@Test
	@DisplayName("케이크 샵 하트 취소를 성공한다")
	void heartCakeShop2() {
		//given
		final User user = getUserFixture(Role.USER);
		final CakeShop cakeShop = getCakeShopFixture();
		userHeartFacade.heartCakeShop(user, cakeShop);

		//when
		userHeartFacade.heartCakeShop(user, cakeShop);

		//then
		assertThat(cakeShop.isHeartedBy(user)).isFalse();
	}
}

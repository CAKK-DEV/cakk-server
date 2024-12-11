package com.cakk.core.facade

import io.kotest.matchers.shouldBe

import org.assertj.core.api.Assertions
import org.mockito.InjectMocks

import com.cakk.common.enums.Role
import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.FacadeTest
import com.cakk.core.facade.user.UserHeartFacade

internal class UserEntityHeartFacadeTest : FacadeTest() {

	@InjectMocks
	private lateinit var userHeartFacade: UserHeartFacade

	@TestWithDisplayName("케이크 하트를 성공한다")
	fun heartCake() {
		//given
		val user = getUserFixture(Role.USER)
		val cake = getCakeFixture()

		//when
		userHeartFacade.heartCake(user, cake)

		//then
		Assertions.assertThat(cake.isHeartedBy(user)).isTrue()
	}

	@TestWithDisplayName("케이크 하트 취소를 성공한다")
	fun heartCake2() {
		//given
		val user = getUserFixture(Role.USER)
		val cake = getCakeFixture()
		userHeartFacade.heartCake(user, cake)

		//when
		userHeartFacade.heartCake(user, cake)

		//then
		cake.isHeartedBy(user) shouldBe false
	}

	@TestWithDisplayName("케이크 샵 하트를 성공한다")
	fun heartCakeShop() {
		//given
		val user = getUserFixture(Role.USER)
		val cakeShop = getCakeShopFixture()

		//when
		userHeartFacade.heartCakeShop(user, cakeShop)

		//then
		cakeShop.isHeartedBy(user) shouldBe true
	}

	@TestWithDisplayName("케이크 샵 하트 취소를 성공한다")
	fun heartCakeShop2() {
		//given
		val user = getUserFixture(Role.USER)
		val cakeShop = getCakeShopFixture()
		userHeartFacade.heartCakeShop(user, cakeShop)

		//when
		userHeartFacade.heartCakeShop(user, cakeShop)

		//then
		cakeShop.isHeartedBy(user) shouldBe false
	}
}

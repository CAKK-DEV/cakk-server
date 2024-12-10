package com.cakk.core.facade

import java.util.stream.IntStream

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import org.assertj.core.api.Assertions
import org.mockito.InjectMocks

import com.cakk.common.enums.ReturnCode
import com.cakk.common.enums.Role
import com.cakk.common.exception.CakkException
import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.FacadeTest
import com.cakk.core.facade.user.UserLikeFacade
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity

internal class UserEntityLikeFacadeTest : FacadeTest() {

	@InjectMocks
	private lateinit var userLikeFacade: UserLikeFacade

	@TestWithDisplayName("케이크 샵 기대돼요 동작을 성공한다")
	fun likeCakeShop() {
		//given
		val userEntity: UserEntity = getUserFixture(Role.USER)
		val cakeShop: CakeShopEntity = getCakeShopFixture()

		//when
		userLikeFacade.likeCakeShop(userEntity, cakeShop)

		//then
		Assertions.assertThat(cakeShop.likeCount).isEqualTo(1)
		val liked: Boolean = cakeShop.shopLikes.stream().anyMatch { it.user == userEntity }
		liked shouldBe true
	}

	@TestWithDisplayName("케이크 샵 기대돼요 동작이 50회 초과로 실패한다")
	fun heartCakeShop2() {
		// given
		val userEntity: UserEntity = getUserFixture(Role.USER)
		val cakeShop: CakeShopEntity = getCakeShopFixture()

		IntStream.range(0, 50).forEach { userLikeFacade.likeCakeShop(userEntity, cakeShop) }

		// when
		val exception = shouldThrow<CakkException> {
			userLikeFacade.likeCakeShop(userEntity, cakeShop)
		}

		// then
		exception.getReturnCode() shouldBe ReturnCode.MAX_CAKE_SHOP_LIKE
	}
}

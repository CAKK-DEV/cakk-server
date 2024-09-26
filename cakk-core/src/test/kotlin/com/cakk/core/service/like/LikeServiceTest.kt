package com.cakk.core.service.like

import io.kotest.assertions.throwables.shouldNotThrow

import org.junit.jupiter.api.DisplayName
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn

import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.MockitoTest
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.core.common.fixture.FixtureCommon.getLongFixtureGoe
import com.cakk.core.common.fixture.FixtureCommon.getPointFixture
import com.cakk.core.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.user.UserLikeFacade
import com.cakk.domain.mysql.entity.shop.CakeShop

@DisplayName("좋아요 기능 관련 비즈니스 로직 테스트")
internal class LikeServiceTest : MockitoTest() {

	@InjectMocks
	private lateinit var likeService: LikeService

	@Mock
	private lateinit var cakeShopReadFacade: CakeShopReadFacade

	@Mock
	private lateinit var userLikeFacade: UserLikeFacade

	@TestWithDisplayName("케이크 샵 좋아요를 성공한다.")
	fun likeCakeShop() {
		// given
		val user = getUserFixture()
		val cakeShopId: Long = getLongFixtureGoe(1).sample()
		val cakeShop = fixtureMonkey.giveMeBuilder(CakeShop::class.java)
			.set("shopName", getStringFixtureBw(10, 30))
			.set("shopBio", getStringFixtureBw(10, 40))
			.set("shopDescription", getStringFixtureBw(100, 500))
			.set("location", getPointFixture())
			.sample()

		doReturn(cakeShop).`when`(cakeShopReadFacade).findByIdWithLike(cakeShopId)
		doNothing().`when`(userLikeFacade).likeCakeShop(user, cakeShop)

		// when
		likeService.likeCakeShop(user, cakeShopId)

		// then
		shouldNotThrow<Exception> { likeService.likeCakeShop(user, cakeShopId) }
	}
}

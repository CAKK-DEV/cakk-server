package com.cakk.core.service.like

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

import org.junit.jupiter.api.DisplayName
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.MockitoTest
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.core.common.fixture.FixtureCommon.getLongFixtureBw
import com.cakk.core.common.fixture.FixtureCommon.getLongFixtureGoe
import com.cakk.core.common.fixture.FixtureCommon.getPointFixture
import com.cakk.core.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.core.dto.param.search.HeartCakeSearchParam
import com.cakk.core.facade.cake.CakeReadFacade
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.cake.CakeShopUserReadFacade
import com.cakk.core.facade.user.UserHeartFacade
import com.cakk.infrastructure.persistence.param.like.HeartCakeImageResponseParam
import com.cakk.infrastructure.persistence.entity.cake.Cake
import com.cakk.infrastructure.persistence.entity.shop.CakeShop
import com.cakk.infrastructure.persistence.entity.user.User

@DisplayName("하트 기능 관련 비즈니스 로직 테스트")
internal class HeartServiceTest : MockitoTest() {

	@InjectMocks
	private lateinit var heartService: HeartService

	@Mock
	private lateinit var cakeReadFacade: CakeReadFacade

	@Mock
	private lateinit var cakeShopReadFacade: CakeShopReadFacade

	@Mock
	private lateinit var cakeShopUserReadFacade: CakeShopUserReadFacade

	@Mock
	private lateinit var userHeartFacade: UserHeartFacade


	@TestWithDisplayName("하트 한 케이크 목록을 조회한다.")
	fun findCakeImagesByCursorAndHeart() {
		val user = getUserFixture()
		val param = HeartCakeSearchParam(null, 5, user)
		val cakeImages = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.param.like.HeartCakeImageResponseParam::class.java)
			.set("cakeShopId", getLongFixtureGoe(1))
			.set("cakeId", getLongFixtureGoe(1))
			.set("cakeHeartId", getLongFixtureGoe(1))
			.set("cakeImageUrl", getStringFixtureBw(100, 200))
			.sampleList(5)

		doReturn(cakeImages).`when`(cakeShopUserReadFacade).searchCakeImagesByCursorAndHeart(param.cakeHeartId, user.id, param.pageSize)

		// when
		val result = heartService.searchCakeImagesByCursorAndHeart(param)

		// then
		result.cakeImages shouldBe cakeImages
		result.lastCakeHeartId shouldNotBe null

		verify(cakeShopUserReadFacade, times(1)).searchCakeImagesByCursorAndHeart(param.cakeHeartId, user.id, param.pageSize)
	}

	@TestWithDisplayName("하트 한 케이크 목록 n번째 페이지를 조회한다.")
	fun findCakeImagesByCursorAndHeart2() {
		val user = getUserFixture()
		val param = HeartCakeSearchParam(12L, 5, user)
		val cakeImages = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.param.like.HeartCakeImageResponseParam::class.java)
			.set("cakeShopId", getLongFixtureGoe(1))
			.set("cakeId", getLongFixtureGoe(1))
			.set("cakeHeartId", getLongFixtureBw(1, 11))
			.set("cakeImageUrl", getStringFixtureBw(100, 200))
			.sampleList(5)

		doReturn(cakeImages).`when`(cakeShopUserReadFacade).searchCakeImagesByCursorAndHeart(param.cakeHeartId, user.id, param.pageSize)

		// when
		val result = heartService.searchCakeImagesByCursorAndHeart(param)

		// then
		result.cakeImages shouldBe cakeImages
		result.lastCakeHeartId shouldNotBe null

		verify(cakeShopUserReadFacade, times(1)).searchCakeImagesByCursorAndHeart(param.cakeHeartId, user.id, param.pageSize)
	}

	@TestWithDisplayName("하트 한 케이크가 없을 때 목록 조회 시 빈 배열을 반환한다.")
	fun findCakeImagesByCursorAndHeart3() {
		val user: com.cakk.infrastructure.persistence.entity.user.User = getUserFixture()
		val param = HeartCakeSearchParam(5L, 5, user)

		doReturn(listOf<com.cakk.infrastructure.persistence.param.like.HeartCakeImageResponseParam>()).`when`(cakeShopUserReadFacade)
			.searchCakeImagesByCursorAndHeart(param.cakeHeartId, user.id, param.pageSize)

		// when
		val result = heartService.searchCakeImagesByCursorAndHeart(param)

		// then
		result.cakeImages shouldHaveSize 0
		result.lastCakeHeartId shouldBe null

		verify(cakeShopUserReadFacade, times(1)).searchCakeImagesByCursorAndHeart(param.cakeHeartId, user.id, param.pageSize)
	}

	@TestWithDisplayName("케이크에 대하여 하트를 동작한다.")
	fun heartCake1() {
		// given
		val user = getUserFixture()
		val cakeId = 1L
		val cake = fixtureMonkey.giveMeOne(com.cakk.infrastructure.persistence.entity.cake.Cake::class.java)

		doReturn(cake).`when`(cakeReadFacade).findByIdWithHeart(cakeId)
		doNothing().`when`(userHeartFacade).heartCake(user, cake)

		// when & then
		shouldNotThrow<Exception> { heartService.heartCake(user, cakeId) }

		verify(cakeReadFacade, times(1)).findByIdWithHeart(cakeId)
		verify(userHeartFacade, times(1)).heartCake(user, cake)
	}

	@TestWithDisplayName("케이크에 대하여 하트 취소를 동작한다.")
	fun heartCake2() {
		// given
		val user = getUserFixture()
		val cakeId = 1L
		val cake = fixtureMonkey.giveMeOne(com.cakk.infrastructure.persistence.entity.cake.Cake::class.java)
		cake.heart(user)

		doReturn(cake).`when`(cakeReadFacade).findByIdWithHeart(cakeId)
		doNothing().`when`(userHeartFacade).heartCake(user, cake)

		// when & then
		shouldNotThrow<Exception> { heartService.heartCake(user, cakeId) }

		verify(cakeReadFacade, times(1)).findByIdWithHeart(cakeId)
		verify(userHeartFacade, times(1)).heartCake(user, cake)
	}

	@TestWithDisplayName("해당 케이크가 없으면 하트 동작을 실패한다.")
	fun heartCake3() {
		// given
		val user = getUserFixture()
		val cakeId = 1L

		doThrow(CakkException(ReturnCode.NOT_EXIST_CAKE)).`when`(cakeReadFacade).findByIdWithHeart(cakeId)

		// when
		val exception = shouldThrow<CakkException> { heartService.heartCake(user, cakeId) }
		// then
		exception.getReturnCode() shouldBe ReturnCode.NOT_EXIST_CAKE

		verify(cakeReadFacade, times(1)).findByIdWithHeart(cakeId)
	}

	@TestWithDisplayName("케이크 샵에 대하여 하트를 동작한다.")
	fun heartCakeShop1() {
		// given
		val user = getUserFixture()
		val cakeShopId = 1L
		val cakeShop = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.entity.shop.CakeShop::class.java)
			.set("shopName", getStringFixtureBw(10, 30))
			.set("shopBio", getStringFixtureBw(10, 40))
			.set("shopDescription", getStringFixtureBw(100, 500))
			.set("location", getPointFixture())
			.sample()

		doReturn(cakeShop).`when`(cakeShopReadFacade).findByIdWithHeart(cakeShopId)
		doNothing().`when`(userHeartFacade).heartCakeShop(user, cakeShop)

		// when & then
		shouldNotThrow<Exception> { heartService.heartCakeShop(user, cakeShopId) }

		verify(cakeShopReadFacade, times(1)).findByIdWithHeart(cakeShopId)
		verify(userHeartFacade, times(1)).heartCakeShop(user, cakeShop)
	}

	@TestWithDisplayName("해당 케이크 샵이 없으면 하트 동작을 실패한다.")
	fun heartCakeShop3() {
		// given
		val user = getUserFixture()
		val cakeShopId = 1L

		doThrow(CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)).`when`(cakeShopReadFacade).findByIdWithHeart(cakeShopId)

		// when
		val exception = shouldThrow<CakkException> { heartService.heartCakeShop(user, cakeShopId) }

		// then
		exception.getReturnCode() shouldBe ReturnCode.NOT_EXIST_CAKE_SHOP

		verify(cakeShopReadFacade, times(1)).findByIdWithHeart(cakeShopId)
	}
}

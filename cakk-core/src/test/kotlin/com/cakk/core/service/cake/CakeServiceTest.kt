package com.cakk.core.service.cake

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

import org.junit.jupiter.api.DisplayName
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.context.ApplicationEventPublisher

import com.cakk.common.enums.CakeDesignCategory
import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.MockitoTest
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.core.common.fixture.FixtureCommon.getLongFixtureGoe
import com.cakk.core.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.core.dto.param.cake.CakeSearchByCategoryParam
import com.cakk.core.dto.param.cake.CakeSearchByShopParam
import com.cakk.core.dto.param.cake.CakeSearchByViewsParam
import com.cakk.core.facade.cake.CakeManageFacade
import com.cakk.core.facade.cake.CakeReadFacade
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.tag.TagReadFacade
import com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam

@DisplayName("케이크 조회 관련 비즈니스 로직 테스트")
internal class CakeServiceTest : MockitoTest() {

	@InjectMocks
	private lateinit var cakeService: CakeService

	@Mock
	private lateinit var cakeReadFacade: CakeReadFacade

	@Mock
	private lateinit var tagReadFacade: TagReadFacade

	@Mock
	private lateinit var cakeShopReadFacade: CakeShopReadFacade

	@Mock
	private lateinit var cakeManageFacade: CakeManageFacade

	@Mock
	private lateinit var eventPublisher: ApplicationEventPublisher

	@TestWithDisplayName("카테고리에 해당하는 케이크 목록을 조회한다")
	fun findCakeImagesByCursorAndCategory1() {
		// given
		val dto = CakeSearchByCategoryParam(null, CakeDesignCategory.FLOWER, 3)
		val cakeImages = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam::class.java)
			.set("cakeId", getLongFixtureGoe(1))
			.set("cakeShopId", getLongFixtureGoe(1))
			.set("cakeImageUrl", getStringFixtureBw(10, 20))
			.set("thumbnailUrl", getStringFixtureBw(5, 10))
			.set("shopName", getStringFixtureBw(5, 10))
			.sampleList(3)

		doReturn(cakeImages).`when`(cakeReadFacade).searchCakeImagesByCursorAndCategory(dto.cakeId, dto.category, dto.pageSize)

		// when
		val result = cakeService.findCakeImagesByCursorAndCategory(dto)

		// then
		result.cakeImages shouldBe cakeImages
		result.lastCakeId shouldNotBe null

		verify(cakeReadFacade, times(1)).searchCakeImagesByCursorAndCategory(dto.cakeId, dto.category, dto.pageSize)
	}

	@TestWithDisplayName("카테고리에 해당하는 케이크가 없을 시 빈 배열을 리턴한다.")
	fun findCakeImagesByCursorAndCategory2() {
		// given
		val dto = CakeSearchByCategoryParam(null, CakeDesignCategory.FLOWER, 3)
		val cakeImages = fixtureMonkey.giveMe(com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam::class.java, 0)

		doReturn(cakeImages).`when`(cakeReadFacade).searchCakeImagesByCursorAndCategory(dto.cakeId, dto.category, dto.pageSize)

		// when
		val result = cakeService.findCakeImagesByCursorAndCategory(dto)

		// then
		result.cakeImages shouldBe cakeImages
		result.lastCakeId shouldBe null

		verify(cakeReadFacade, times(1)).searchCakeImagesByCursorAndCategory(dto.cakeId, dto.category, dto.pageSize)
	}

	@TestWithDisplayName("케이크 샵에 속한 케이크 목록을 조회한다")
	fun findCakeImagesByCursorAndShop1() {
		// given
		val dto = CakeSearchByShopParam(null, 1L, 3)
		val cakeImages = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam::class.java)
			.set("cakeId", getLongFixtureGoe(1))
			.set("cakeShopId", getLongFixtureGoe(1))
			.set("cakeImageUrl", getStringFixtureBw(10, 20))
			.set("thumbnailUrl", getStringFixtureBw(5, 10))
			.set("shopName", getStringFixtureBw(5, 10))
			.sampleList(3)

		doReturn(cakeImages).`when`(cakeReadFacade).searchCakeImagesByCursorAndCakeShopId(dto.cakeId, dto.shopId, dto.pageSize)

		// when
		val result = cakeService.findCakeImagesByCursorAndCakeShopId(dto)

		// then
		result.cakeImages shouldBe cakeImages
		result.lastCakeId shouldNotBe null

		verify(cakeReadFacade, times(1)).searchCakeImagesByCursorAndCakeShopId(dto.cakeId, dto.shopId, dto.pageSize)
	}

	@TestWithDisplayName("케이크 샵에 속한 케이크가 없을 시 빈 배열을 리턴한다.")
	fun findCakeImagesByCursorAndShop2() {
		// given
		val dto = CakeSearchByShopParam(null, 1L, 3)
		val cakeImages = fixtureMonkey.giveMe(com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam::class.java, 0)

		doReturn(cakeImages).`when`(cakeReadFacade).searchCakeImagesByCursorAndCakeShopId(dto.cakeId, dto.shopId, dto.pageSize)

		// when
		val result = cakeService.findCakeImagesByCursorAndCakeShopId(dto)

		// then
		result.cakeImages shouldBe cakeImages
		result.lastCakeId shouldBe null

		verify(cakeReadFacade, times(1)).searchCakeImagesByCursorAndCakeShopId(dto.cakeId, dto.shopId, dto.pageSize)
	}

	@TestWithDisplayName("인기 케이크 목록을 조회한다")
	fun searchCakeImagesByCursorAndViews1() {
		// given
		val cursor = 0L
		val pageSize = 3
		val dto = CakeSearchByViewsParam(cursor, pageSize)
		val cakeIds = listOf(1L, 2L, 3L)
		val cakeImages = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam::class.java)
			.set("cakeId", getLongFixtureGoe(1))
			.set("cakeShopId", getLongFixtureGoe(1))
			.set("cakeImageUrl", getStringFixtureBw(10, 20))
			.set("thumbnailUrl", getStringFixtureBw(5, 10))
			.set("shopName", getStringFixtureBw(5, 10))
			.sampleList(3)

		doReturn(Pair(cakeIds, cakeImages)).`when`(cakeReadFacade).searchBestCakeImages(cursor, pageSize)

		// when
		val result = cakeService.searchCakeImagesByCursorAndViews(dto)

		// then
		result.cakeImages shouldNotBe null
		result.lastCakeId shouldBe null

		verify(cakeReadFacade, times(1)).searchBestCakeImages(cursor, pageSize)
	}

	@TestWithDisplayName("인기 케이크 목록이 없을 시 빈 배열을 조회한다")
	fun searchCakeImagesByCursorAndViews2() {
		// given
		val cursor = 0L
		val pageSize = 3
		val dto = CakeSearchByViewsParam(cursor, pageSize)

		doReturn(Pair(listOf<Long>(), listOf<CakeSearchByViewsParam>())).`when`(cakeReadFacade).searchBestCakeImages(cursor, pageSize)

		// when
		val result = cakeService.searchCakeImagesByCursorAndViews(dto)

		// then
		result.cakeImages.size shouldBe 0
		result.lastCakeId shouldBe null

		verify(cakeReadFacade, times(1)).searchBestCakeImages(cursor, pageSize)
	}
}

package com.cakk.core.service.shop

import java.time.LocalTime

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

import org.junit.jupiter.api.DisplayName
import org.mockito.InjectMocks
import org.mockito.kotlin.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.context.ApplicationEventPublisher

import com.cakk.common.enums.Days
import com.cakk.common.enums.LinkKind
import com.cakk.common.enums.ReturnCode
import com.cakk.common.enums.VerificationStatus
import com.cakk.common.exception.CakkException
import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.MockitoTest
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.core.common.fixture.FixtureCommon.getEnumFixture
import com.cakk.core.common.fixture.FixtureCommon.getLongFixtureGoe
import com.cakk.core.common.fixture.FixtureCommon.getPointFixture
import com.cakk.core.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.core.common.fixture.FixtureCommon.getStringFixtureEq
import com.cakk.core.dto.param.search.CakeShopSearchByViewsParam
import com.cakk.core.dto.param.shop.CreateShopParam
import com.cakk.core.dto.param.shop.PromotionParam
import com.cakk.core.dto.param.shop.ShopLinkParam
import com.cakk.core.dto.param.shop.ShopOperationParam
import com.cakk.core.facade.cake.BusinessInformationReadFacade
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.shop.CakeShopManageFacade
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.core.mapper.supplyCakeShopDetailResponseBy
import com.cakk.core.mapper.supplyCakeShopInfoResponseBy
import com.cakk.core.mapper.supplyCakeShopSimpleResponseBy
import com.cakk.domain.mysql.bo.user.VerificationPolicy
import com.cakk.domain.mysql.dto.param.shop.*
import com.cakk.domain.mysql.dto.param.user.CertificationParam
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.user.BusinessInformation
import com.cakk.domain.mysql.event.shop.CertificationEvent
import com.cakk.domain.redis.repository.CakeShopViewsRedisRepository
import org.springframework.test.util.ReflectionTestUtils

@DisplayName("케이크 샵 조회 관련 비즈니스 로직 테스트")
internal class ShopServiceTest : MockitoTest() {

	@InjectMocks
	private lateinit var shopService: ShopService

	@Mock
	private lateinit var userReadFacade: UserReadFacade

	@Mock
	private lateinit var cakeShopReadFacade: CakeShopReadFacade

	@Mock
	private lateinit var businessInformationReadFacade: BusinessInformationReadFacade

	@Mock
	private lateinit var cakeShopManageFacade: CakeShopManageFacade

	@Mock
	private lateinit var cakeShopViewsRedisRepository: CakeShopViewsRedisRepository

	@Mock
	private lateinit var publisher: ApplicationEventPublisher

	@Mock
	private lateinit var verificationPolicy: VerificationPolicy

	private fun getCreateShopParamFixture(): CreateShopParam {
		return fixtureMonkey.giveMeBuilder(CreateShopParam::class.java)
			.set("businessNumber", getStringFixtureBw(1, 7))
			.set(
				"operationDays", listOf(
					ShopOperationParam(Days.MON, LocalTime.now(), LocalTime.now()),
					ShopOperationParam(Days.TUE, LocalTime.now(), LocalTime.now())
				)
			)
			.setNotNull("shopName")
			.setNotNull("shopAddress")
			.setNotNull("latitude")
			.setNotNull("longitude")
			.set("links", listOf(ShopLinkParam(LinkKind.WEB, "cake-shop.com")))
			.set("shopBio", getStringFixtureEq(20))
			.set("shopDescription", getStringFixtureEq(20))
			.sample()
	}

	private fun getPromotionParamFixture(): PromotionParam {
		return fixtureMonkey.giveMeBuilder(PromotionParam::class.java)
			.set("userId", getLongFixtureGoe(10))
			.set("cakeShopId", getLongFixtureGoe(0))
			.sample()
	}

	private fun getCertificationParamFixture(isNull: Boolean): CertificationParam {
		val builder = fixtureMonkey.giveMeBuilder(CertificationParam::class.java).setNotNull("user")

		return when {
			isNull -> builder.setNull("cakeShopId").sample()
			else -> builder.setNotNull("cakeShopId").sample()
		}
	}

	private fun getCakeShopFixture(): CakeShop {
		val cakeShop = fixtureMonkey.giveMeBuilder(CakeShop::class.java)
			.set("shopName", getStringFixtureEq(30))
			.set("shopBio", getStringFixtureEq(40))
			.set("shopDescription", getStringFixtureEq(500))
			.set("location", getPointFixture())
			.sample()
		ReflectionTestUtils.setField(cakeShop, "id", getLongFixtureGoe(1).sample())
		return cakeShop
	}

	private fun getBusinessInformationFixture(): BusinessInformation {
		return fixtureMonkey.giveMeBuilder(BusinessInformation::class.java)
			.set("businessNumber", getStringFixtureEq(20))
			.set("cakeShop", getCakeShopFixture())
			.set("verificationStatus", VerificationStatus.UNREQUESTED)
			.sample()
	}

	private fun getCertificationEventFixture(): CertificationEvent {
		return fixtureMonkey.giveMeBuilder(CertificationEvent::class.java).sample()
	}

	@TestWithDisplayName("id로 케이크 샵을 간단조회 한다.")
	fun searchSimpleById1() {
		// given
		val cakeShopId = 1L
		val response = fixtureMonkey.giveMeBuilder(CakeShopSimpleParam::class.java)
			.set("cakeShopId", getLongFixtureGoe(10))
			.set("thumbnailUrl", getStringFixtureBw(100, 200))
			.set("cakeShopName", getStringFixtureBw(1, 30))
			.set("cakeShopBio", getStringFixtureBw(1, 40))
			.sample()

		doReturn(response).`when`(cakeShopReadFacade).searchSimpleById(cakeShopId)

		// when
		val result = shopService.searchSimpleById(cakeShopId)

		// then
		result shouldBe supplyCakeShopSimpleResponseBy(response)

		verify(cakeShopReadFacade, times(1)).searchSimpleById(cakeShopId)
	}

	@TestWithDisplayName("id에 해당하는 케이크 샵이 없으면 간단조회 시, 에러를 반환한다.")
	fun searchSimpleById2() {
		// given
		val cakeShopId: Long = 1L

		doThrow(CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)).`when`(cakeShopReadFacade).searchSimpleById(cakeShopId)

		// then
		val exception = shouldThrow<CakkException> {
			shopService.searchSimpleById(cakeShopId)
		}

		exception.getReturnCode() shouldBe ReturnCode.NOT_EXIST_CAKE_SHOP
		verify(cakeShopReadFacade, times(1)).searchSimpleById(cakeShopId)
	}

	@TestWithDisplayName("id로 케이크 샵을 상세조회 한다.")
	fun searchDetailById1() {
		// given
		val cakeShopId = 1L
		val param = fixtureMonkey.giveMeBuilder(CakeShopDetailParam::class.java)
			.set("cakeShopId", getLongFixtureGoe(10))
			.set("shopName", getStringFixtureBw(1, 30))
			.set("thumbnailUrl", getStringFixtureBw(100, 200))
			.set("shopBio", getStringFixtureBw(1, 10))
			.set("shopDescription", getStringFixtureBw(100, 500))
			.set("operationDays", setOf(getEnumFixture(Days::class.java), getEnumFixture(Days::class.java)))
			.set("links", setOf(CakeShopLinkParam(LinkKind.WEB, getStringFixtureEq(10).sample())))
			.sample()

		doReturn(param).`when`(cakeShopReadFacade).searchDetailById(cakeShopId)

		// when
		val result = shopService.searchDetailById(cakeShopId)

		// then
		result shouldBe supplyCakeShopDetailResponseBy(param)

		verify(cakeShopReadFacade, times(1)).searchDetailById(cakeShopId)
	}

	@TestWithDisplayName("id에 해당하는 케이크 샵이 없으면 상세조회 시, 에러를 반환한다.")
	fun searchDetailById2() {
		// given
		val cakeShopId = 1L

		doThrow(CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)).`when`(cakeShopReadFacade).searchDetailById(cakeShopId)

		// then
		val exception = shouldThrow<CakkException> {
			shopService.searchDetailById(cakeShopId)
		}

		exception.getReturnCode() shouldBe ReturnCode.NOT_EXIST_CAKE_SHOP
		verify(cakeShopReadFacade, times(1)).searchDetailById(cakeShopId)
	}

	@TestWithDisplayName("Admin에 의해 케이크 샵을 생성하는데 성공한다")
	fun createCakeShop() {
		//given
		val param = getCreateShopParamFixture()
		val cakeShop = getCakeShopFixture()

		doReturn(cakeShop).`when`(cakeShopManageFacade).create(any(), any(), any(), any())

		//when
		val response = shopService.createCakeShopByCertification(param)

		//verify
		response.cakeShopId shouldBe cakeShop.id
		verify(cakeShopManageFacade, times(1)).create(any(), any(), any(), any())
	}

	@TestWithDisplayName("userId와 cakeShopId가 존재한다면, 해당 userId의 사용자는 Owner로 승격된다")
	fun promoteUser() {
		// given
		val param: PromotionParam = getPromotionParamFixture()
		val businessInformation = getBusinessInformationFixture()

		doReturn(getUserFixture()).`when`(userReadFacade).findByUserId(param.userId)
		doReturn(businessInformation).`when`(cakeShopReadFacade).findBusinessInformationWithShop(param.cakeShopId)

		//when,then
		shopService.promoteUserToBusinessOwner(param)

		//verify
		verify(userReadFacade, times(1)).findByUserId(param.userId)
		verify(cakeShopReadFacade, times(1)).findBusinessInformationWithShop(param.cakeShopId)
	}

	@TestWithDisplayName("cakeShopId가 존재한다면, 정보를 찾아서 이벤트를 발행한다")
	fun requestCertificationEventWithInfo() {
		//given
		val param = getCertificationParamFixture(false)

		doReturn(getBusinessInformationFixture()).`when`(cakeShopReadFacade).findBusinessInformationByCakeShopId(param.cakeShopId)
		doReturn(getCertificationEventFixture()).`when`(verificationPolicy).requestCertificationBusinessOwner(any(), any())

		//when
		shopService.requestCertificationBusinessOwner(param)

		//verify
		verify(cakeShopReadFacade, times(1)).findBusinessInformationByCakeShopId(param.cakeShopId)
	}

	@TestWithDisplayName("id로 케이크 샵을 상세정보를 조회한다.")
	fun searchInfoById1() {
		// given
		val cakeShopId = 1L
		val param = fixtureMonkey.giveMeBuilder(CakeShopInfoParam::class.java)
			.set("shopAddress", getStringFixtureBw(1, 30))
			.set("point", getPointFixture())
			.set(
				"operationDays", listOf(
					ShopOperationParam(Days.MON, LocalTime.now(), LocalTime.now()),
					ShopOperationParam(Days.TUE, LocalTime.now(), LocalTime.now())
				)
			)
			.sample()

		doReturn(param).`when`(cakeShopReadFacade).searchInfoById(cakeShopId)

		// when
		val result = shopService.searchInfoById(cakeShopId)

		// then
		result shouldBe supplyCakeShopInfoResponseBy(param)

		verify(cakeShopReadFacade, times(1)).searchInfoById(cakeShopId)
	}

	@TestWithDisplayName("id에 해당하는 케이크 샵이 없으면 상세조회 시, 에러를 반환한다.")
	fun searchInfoById2() {
		// given
		val cakeShopId: Long = 1L

		doThrow(CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)).`when`(cakeShopReadFacade).searchInfoById(cakeShopId)

		// then
		val exception = shouldThrow<CakkException> {
			shopService.searchInfoById(cakeShopId)
		}

		exception.getReturnCode() shouldBe ReturnCode.NOT_EXIST_CAKE_SHOP
		verify(cakeShopReadFacade, times(1)).searchInfoById(cakeShopId)
	}

	@TestWithDisplayName("인기 케이크 샵 목록을 조회한다.")
	fun searchCakeShopsByCursorAndViews1() {
		// given
		val offset: Long = 0L
		val pageSize: Int = 3
		val param = CakeShopSearchByViewsParam(offset, pageSize)
		val cakeShopIds = listOf(1L, 2L, 3L)
		val cakeShops = getConstructorMonkey().giveMeBuilder(CakeShop::class.java)
			.set("cakeShopId", getLongFixtureGoe(1))
			.set("thumbnailUrl", getStringFixtureBw(100, 200))
			.set("cakeShopName", getStringFixtureBw(1, 30))
			.set("cakeShopBio", getStringFixtureBw(1, 40))
			.sampleList(3)

		doReturn(cakeShopIds).`when`(cakeShopViewsRedisRepository).findTopShopIdsByOffsetAndCount(offset, pageSize.toLong())
		doReturn(cakeShops).`when`(cakeShopReadFacade).searchShopsByShopIds(cakeShopIds)

		// when
		val result = shopService.searchCakeShopsByCursorAndViews(param)

		// then
		result shouldNotBe null
		result.size shouldBe cakeShops.size

		verify(cakeShopViewsRedisRepository, times(1)).findTopShopIdsByOffsetAndCount(offset, pageSize.toLong())
		verify(cakeShopReadFacade, times(1)).searchShopsByShopIds(cakeShopIds)
	}

	@TestWithDisplayName("인기 케이크 샵 목록이 없는 경우, 빈 배열을 조회한다.")
	fun searchCakeShopsByCursorAndViews2() {
		// given
		val offset = 0L
		val pageSize = 3
		val param = CakeShopSearchByViewsParam(offset, pageSize)
		val cakeShopIds: List<Long> = listOf()

		doReturn(cakeShopIds).`when`(cakeShopViewsRedisRepository).findTopShopIdsByOffsetAndCount(offset, pageSize.toLong())

		// when
		val result = shopService.searchCakeShopsByCursorAndViews(param)

		// then
		result shouldNotBe null
		result.cakeShops shouldHaveSize 0

		verify(cakeShopViewsRedisRepository, times(1)).findTopShopIdsByOffsetAndCount(offset, pageSize.toLong())
		verify(cakeShopReadFacade, never()).searchShopsByShopIds(cakeShopIds)
	}

	@TestWithDisplayName("케이크샵 기본 정보 수정을 한다")
	fun updateCakeShopBasicInformation() {
		//given
		val cakeShopUpdateParam = fixtureMonkey.giveMeBuilder(CakeShopUpdateParam::class.java)
			.set("cakeShopId", getLongFixtureGoe(1))
			.set("user", getUserFixture())
			.sample()
		val cakeShopFixture = getCakeShopFixture()

		doReturn(cakeShopFixture).`when`(cakeShopReadFacade).searchByIdAndOwner(cakeShopUpdateParam.cakeShopId, cakeShopUpdateParam.user)

		//when, then
		shopService.updateBasicInformation(cakeShopUpdateParam)

		//verify
		verify(cakeShopReadFacade, times(1)).searchByIdAndOwner(cakeShopUpdateParam.cakeShopId, cakeShopUpdateParam.user)
	}
}

package com.cakk.api.integration.shop

import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveAtMostSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatusCode
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.web.util.UriComponentsBuilder

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.IntegrationTest
import com.cakk.core.dto.param.shop.ShopOperationParam
import com.cakk.api.dto.request.link.UpdateLinkRequest
import com.cakk.api.dto.request.operation.UpdateShopOperationRequest
import com.cakk.api.dto.request.shop.UpdateShopAddressRequest
import com.cakk.api.dto.request.shop.UpdateShopRequest
import com.cakk.api.dto.request.user.CertificationRequest
import com.cakk.api.dto.response.like.HeartResponse
import com.cakk.api.dto.response.shop.*
import com.cakk.common.enums.ReturnCode
import com.cakk.common.response.ApiResponse
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.domain.mysql.dto.param.shop.CakeShopLinkParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopOperationParam
import com.cakk.domain.redis.repository.CakeShopViewsRedisRepository
import com.cakk.domain.redis.repository.CakeViewsRedisRepository

@SqlGroup(
	Sql(
		scripts = ["/sql/insert-test-user.sql", "/sql/insert-cake-shop.sql", "/sql/insert-heart.sql"],
		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
	), Sql(
		scripts = ["/sql/delete-all.sql"],
		executionPhase = ExecutionPhase.AFTER_TEST_METHOD
	)
)
internal class ShopIntegrationTest(
	@LocalServerPort private val port: Int
) : IntegrationTest() {

	protected val baseUrl = "$localhost$port/api/v1/shops"

	@Autowired
	private lateinit var cakeShopReadFacade: CakeShopReadFacade

	@Autowired
	private lateinit var cakeViewsRedisRepository: CakeViewsRedisRepository

	@Autowired
	private lateinit var cakeShopViewsRedisRepository: CakeShopViewsRedisRepository

	private fun initializeViews() {
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(2L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(2L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(2L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(3L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(3L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(4L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(5L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(6L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(7L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(7L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(7L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(8L)
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(9L)
	}

	@AfterEach
	fun setUp() {
		cakeViewsRedisRepository.clear()
		cakeShopViewsRedisRepository.clear()
	}

	@TestWithDisplayName("케이크 샵을 간단 조회에 성공한다.")
	fun simple1() {
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/simple")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse::class.java)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopSimpleResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		val cakeShop = cakeShopReadFacade.findById(cakeShopId)
		data.cakeShopId shouldBe cakeShop.id
		data.thumbnailUrl shouldBe cakeShop.thumbnailUrl
		data.cakeShopName shouldBe cakeShop.shopName
		data.cakeShopBio shouldBe cakeShop.shopBio
	}

	@TestWithDisplayName("케이크 샵을 간단 조회 시, 케이크 조회수를 올리는 데 성공한다.")
	fun simple2() {
		val cakeShopId = 1L
		val cakeId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/simple")
			.queryParam("cakeId", cakeId)
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopSimpleResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		val cakeShop = cakeShopReadFacade.findById(cakeShopId)
		data.cakeShopId shouldBe cakeShop.id
		data.thumbnailUrl shouldBe cakeShop.thumbnailUrl
		data.cakeShopName shouldBe cakeShop.shopName
		data.cakeShopBio shouldBe cakeShop.shopBio

		val viewCakeId = cakeViewsRedisRepository.findTopCakeIdsByOffsetAndCount(0, 10)[0]
		viewCakeId shouldBe cakeId
	}

	@TestWithDisplayName("없는 케이크 샵일 경우, 간단 조회에 실패한다.")
	fun simple3() {
		val cakeShopId = 1000L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/simple")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse::class.java)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(400)
		response.returnCode shouldBe ReturnCode.NOT_EXIST_CAKE_SHOP.code
		response.returnMessage shouldBe ReturnCode.NOT_EXIST_CAKE_SHOP.message
	}

	@TestWithDisplayName("케이크 샵을 상세 조회에 성공한다.")
	fun detail1() {
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse::class.java)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopDetailResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		val cakeShop = cakeShopReadFacade.findById(cakeShopId)
		data.cakeShopId shouldBe cakeShop.id
		data.thumbnailUrl shouldBe cakeShop.thumbnailUrl
		data.cakeShopName shouldBe cakeShop.shopName
		data.cakeShopBio shouldBe cakeShop.shopBio
		data.cakeShopDescription shouldBe cakeShop.shopDescription

		val cakeShopOperations = cakeShopReadFacade.findCakeShopOperationsByCakeShopId(cakeShopId)
			.map { it.operationDay }
			.toSet()

		data.operationDays shouldBe cakeShopOperations

		val cakeShopLinks = cakeShopReadFacade.findCakeShopLinksByCakeShopId(cakeShopId)
			.map { CakeShopLinkParam(it.linkKind, it.linkPath) }
			.toSet()
		data.links shouldBe cakeShopLinks
	}

	@TestWithDisplayName("없는 케이크 샵일 경우, 상세 조회에 실패한다.")
	fun detail2() {
		// given
		val cakeShopId = 1000L
		val uriComponents = UriComponentsBuilder
			.fromUriString(baseUrl)
			.path("/{cakeShopId}")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(400)
		response.returnCode shouldBe ReturnCode.NOT_EXIST_CAKE_SHOP.code
		response.returnMessage shouldBe ReturnCode.NOT_EXIST_CAKE_SHOP.message
	}

	@TestWithDisplayName("로그인한 사용자는 자신의 케이크샵이 존재하는 상태에서 사장님 인증을 요청한다")
	fun request2() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/certification")
			.build()
		val request = getConstructorMonkey().giveMeBuilder(CertificationRequest::class.java)
			.set("businessRegistrationImageUrl", getRandomAlpha(1, 40))
			.set("idCardImageUrl", getRandomAlpha(1, 40))
			.set("cakeShopId", 11L)
			.set("emergencyContact", getRandomAlpha(1, 11))
			.set("message", getRandomAlpha(1, 20))
			.sample()

		// when
		val responseEntity = restTemplate.postForEntity(
			uriComponents.toUriString(),
			HttpEntity(request, authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
	}

	@TestWithDisplayName("케이크 샵의 상세 정보 조회에 성공한다.")
	fun detailInfo1() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/info")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse::class.java)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopInfoResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		val cakeShop = cakeShopReadFacade.findById(cakeShopId)
		val longitude = cakeShop.location.x
		val latitude = cakeShop.location.y

		data.shopAddress shouldBe cakeShop.shopAddress
		data.latitude shouldBe latitude
		data.longitude shouldBe longitude

		val cakeShopOperations = cakeShopReadFacade.findCakeShopOperationsByCakeShopId(cakeShopId)
			.map { CakeShopOperationParam(it.operationDay, it.operationStartTime, it.operationEndTime) }
			.toList()
		data.shopOperationDays shouldBe cakeShopOperations
	}

	@TestWithDisplayName("없는 케이크 샵일 경우, 상세정보 조회에 실패한다.")
	fun detailInfo2() {
		// given
		val cakeShopId = 1000L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/info")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse::class.java)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(400)
		response.returnCode shouldBe ReturnCode.NOT_EXIST_CAKE_SHOP.code
		response.returnMessage shouldBe ReturnCode.NOT_EXIST_CAKE_SHOP.message
	}

	@TestWithDisplayName("테스트 sql script 기준으로 사용자 위치를 중심으로 반경 10km 이내의 가게들을 조회한다")
	fun findAllShopsByLocationBased1() {
		val uriComponents = UriComponentsBuilder
			.fromUriString(baseUrl)
			.path("/location-based")
			.queryParam("latitude", 37.2096575)
			.queryParam("longitude", 127.0998228)
			.build()

		//when
		val responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse::class.java)

		//then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopByMapResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeShops shouldHaveAtLeastSize 0
		data.cakeShops.forEach {
			it.cakeImageUrls shouldHaveAtMostSize 4
			it.cakeShopName shouldNotBe null
		}
	}

	@TestWithDisplayName("해당 id의 이전에 하트 누른 케이크샵에 대하여 하트 상태인지 조회에 성공한다.")
	fun isHeartShop1() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/heart")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			HttpEntity<Any>(authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, HeartResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.isHeart shouldBe true
	}

	@TestWithDisplayName("해당 id의 이전에 하트 누르지 않은 케이크샵에 대하여 하트 상태인지 조회에 성공한다.")
	fun isHeartCake2() {
		// given
		val cakeId = 2L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeId}/heart")
			.buildAndExpand(cakeId)

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			HttpEntity<Any>(authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, HeartResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.isHeart shouldBe false
	}

	@TestWithDisplayName("해당 id의 케이크 샵 하트에 성공한다.")
	fun heartCakeShop() {
		// given
		val cakeShopId = 2L
		val uriComponents = UriComponentsBuilder
			.fromUriString(baseUrl)
			.path("/{cakeShopId}/heart")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity<Any>(authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		response.data shouldBe null
	}

	@TestWithDisplayName("해당 id의 케이크 샵 하트 취소에 성공한다.")
	fun heartCancelCake() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/heart")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity<Any>(authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		response.data shouldBe null
	}

	@TestWithDisplayName("해당 id의 케이크 샵 좋아요에 성공한다.")
	fun likeCakeShop() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/like")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity<Any>(authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		response.data shouldBe null
	}

	@TestWithDisplayName("테스트 sql script 기준으로 사용자 위치를 중심으로 반경 10km 이내의 가게들을 조회한다")
	fun findAllShopsByLocationBased2() {
		// given
		val uriComponents = UriComponentsBuilder
			.fromUriString(baseUrl)
			.path("/location-based")
			.queryParam("latitude", 37.543343)
			.queryParam("longitude", 127.052609)
			.build()

		//when
		val responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse::class.java)

		//then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopByMapResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeShops shouldHaveSize 8
		data.cakeShops.forEach {
			it.cakeShopId shouldBeIn listOf(4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L)
			it.cakeImageUrls shouldHaveAtMostSize 4
			it.cakeShopName shouldNotBe null
		}
	}

	@TestWithDisplayName("테스트 sql script 기준 7개의 케이크 샵이 조회된다")
	fun searchCakeShopsByKeywordsWithConditions1() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/search/shops")
			.queryParam("cakeShopId", 11L)
			.queryParam("latitude", 37.543343)
			.queryParam("longitude", 127.052609)
			.queryParam("pageSize", 10)
			.build()

		//when
		val responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse::class.java)

		//then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopSearchResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.size shouldBe 7
		data.cakeShops.forEach {
			it.cakeImageUrls shouldHaveAtMostSize 4
			it.cakeShopId shouldNotBe null
			it.cakeShopName shouldNotBe null
			it.operationDays shouldNotBe null
		}
	}

	@TestWithDisplayName("케이크샵 검색 시, 다양한 동적 조건에 따라 조회된다")
	fun searchCakeShopsByKeywordWithConditions2() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/search/shops")
			.queryParam("keyword", "케이크")
			.queryParam("latitude", 37.2096575)
			.queryParam("longitude", 127.0998228)
			.queryParam("pageSize", 10)
			.build()

		//when
		val responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse::class.java)

		//then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopSearchResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.size shouldBeGreaterThan 0
		data.cakeShops.forEach {
			it.cakeImageUrls shouldHaveAtMostSize 4
			it.cakeShopId shouldNotBe null
			it.cakeShopName shouldNotBe null
			it.operationDays shouldNotBe null
		}
	}

	@TestWithDisplayName("위치 정보 없이 4개의 케이크샵이 조회된다")
	fun searchCakeShopsByKeywordWithConditions3() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/search/shops")
			.queryParam("cakeShopId", 5)
			.queryParam("keyword", "케이크")
			.queryParam("pageSize", 10)
			.build()

		//when
		val responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse::class.java)

		//then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopSearchResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.size shouldBe 4
		data.cakeShops.forEach {
			it.cakeImageUrls shouldHaveAtMostSize 4
			it.cakeShopId shouldNotBe null
			it.cakeShopName shouldNotBe null
			it.operationDays shouldNotBe null
		}
	}

	@TestWithDisplayName("케이크 샵 기본 정보 업데이트에 성공한다")
	fun updateCakeShopDefaultInfo() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}")
			.buildAndExpand(cakeShopId)
		val request = getConstructorMonkey().giveMeBuilder(UpdateShopRequest::class.java).sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity(request, authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
	}

	@TestWithDisplayName("어드민에 의해 케이크 샵 기본 정보 업데이트에 성공한다")
	fun updateCakeShopDefaultInfoByAdmin() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}")
			.buildAndExpand(cakeShopId)
		val request = getConstructorMonkey().giveMeBuilder(UpdateShopRequest::class.java).sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity(request, getAuthHeaderById(10L)),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
	}

	@TestWithDisplayName("케이크샵 외부 링크 업데이트에 성공한다")
	fun updateShopLinks() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder
			.fromUriString(baseUrl)
			.path("/{cakeShopId}/links")
			.buildAndExpand(cakeShopId)
		val request = getConstructorMonkey().giveMeBuilder(UpdateLinkRequest::class.java).sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity(request, authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
	}

	@TestWithDisplayName("어드민에 의해 케이크샵 외부 링크 업데이트에 성공한다")
	fun updateShopLinksByAdmin() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/links")
			.buildAndExpand(cakeShopId)
		val request = getConstructorMonkey().giveMeBuilder(UpdateLinkRequest::class.java).sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity(request, getAuthHeaderById(10L)),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
	}

	@TestWithDisplayName("케이크 샵 주소 업데이트에 성공한다")
	fun updateShopAddress() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/address")
			.buildAndExpand(cakeShopId)
		val request = getConstructorMonkey().giveMeBuilder(UpdateShopAddressRequest::class.java).sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity(request, authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
	}

	@TestWithDisplayName("어드민에 의해 케이크 샵 주소 업데이트에 성공한다")
	fun updateShopAddressByAdmin() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/address")
			.buildAndExpand(cakeShopId)
		val request = getConstructorMonkey().giveMeBuilder(UpdateShopAddressRequest::class.java).sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity(request, getAuthHeaderById(10L)),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
	}

	@TestWithDisplayName("케이크 샵 영업일 업데이트에 성공한다")
	fun updateShopOperationDays() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/operation-days")
			.buildAndExpand(cakeShopId)
		val shopOperationParams = getConstructorMonkey().giveMeBuilder(ShopOperationParam::class.java)
			.setNotNull("operationDay")
			.setNotNull("operationStartTime")
			.setNotNull("operationEndTime")
			.sampleList(6)
		val request = getConstructorMonkey().giveMeBuilder(UpdateShopOperationRequest::class.java)
			.set("operationDays", shopOperationParams)
			.sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity(request, authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
	}

	@TestWithDisplayName("케이크 샵 영업일 업데이트에 성공한다")
	fun updateShopOperationDaysByAdmin() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/operation-days")
			.buildAndExpand(cakeShopId)
		val shopOperationParams = getConstructorMonkey().giveMeBuilder(
			ShopOperationParam::class.java
		)
			.setNotNull("operationDay")
			.setNotNull("operationStartTime")
			.setNotNull("operationEndTime").sampleList(6)
		val request = getConstructorMonkey().giveMeBuilder(
			UpdateShopOperationRequest::class.java
		)
			.set("operationDays", shopOperationParams)
			.sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity(request, getAuthHeaderById(10L)),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
	}

	@TestWithDisplayName("케이크 샵 사업자 연동 여부 True 확인에 성공한다")
	fun isOwnedCakeShop1() {
		// given
		val cakeShopId = 1L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/owner")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			HttpEntity<Any>(authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopOwnerResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.isOwned shouldBe true
	}

	@TestWithDisplayName("케이크 샵 사업자 연동 여부 True 확인에 성공한다")
	fun isOwnedCakeShop2() {
		// given
		val cakeShopId = 2L
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{cakeShopId}/owner")
			.buildAndExpand(cakeShopId)

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			HttpEntity<Any>(authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopOwnerResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.isOwned shouldBe false
	}

	@TestWithDisplayName("나의 케이크 샵 아이디 조회에 성공한다")
	fun myShopIds() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/mine")
			.build()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			HttpEntity<Any>(authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopByMineResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.isExist shouldBe true
		data.cakeShopId shouldBe 1L
	}

	@TestWithDisplayName("조회수로 케이크 샵 조회에 성공한다")
	fun searchByViews1() {
		// given
		val uriComponents = UriComponentsBuilder
			.fromUriString(baseUrl)
			.path("/search/views")
			.queryParam("offset", 0L)
			.queryParam("pageSize", 4)
			.build()

		initializeViews()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopSearchResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.size shouldBe 4
	}

	@TestWithDisplayName("조회한 케이크 샵이 없을 시, 인기 케이크 샵 조회에 빈 배열을 리턴한다")
	fun searchByViews2() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/search/views")
			.queryParam("pageSize", 4)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse::class.java)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopSearchResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
		data.size shouldBe 0
	}
}

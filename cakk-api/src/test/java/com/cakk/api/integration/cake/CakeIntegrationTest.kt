package com.cakk.api.integration.cake

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.ints.shouldNotBeLessThan
import io.kotest.matchers.shouldBe

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
import com.cakk.api.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.api.dto.request.cake.CakeCreateRequest
import com.cakk.api.dto.request.cake.CakeUpdateRequest
import com.cakk.core.dto.response.cake.CakeDetailResponse
import com.cakk.core.dto.response.cake.CakeImageListResponse
import com.cakk.core.dto.response.cake.CakeImageWithShopInfoListResponse
import com.cakk.core.dto.response.like.HeartResponse
import com.cakk.common.enums.CakeDesignCategory
import com.cakk.common.enums.ReturnCode
import com.cakk.common.response.ApiResponse
import com.cakk.core.facade.cake.CakeReadFacade
import com.cakk.domain.redis.repository.CakeViewsRedisRepository

@SqlGroup(
	Sql(
		scripts = ["/sql/insert-test-user.sql", "/sql/insert-cake.sql", "/sql/insert-heart.sql"],
		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
	), Sql(
		scripts = ["/sql/delete-all.sql"],
		executionPhase = ExecutionPhase.AFTER_TEST_METHOD
	)
)
internal class CakeIntegrationTest(
	@LocalServerPort private val port: Int
) : IntegrationTest() {

	protected val baseUrl = "$localhost$port/api/v1/cakes"

	@Autowired
	private lateinit var cakeReadFacade: CakeReadFacade

	@Autowired
	private lateinit var cakeViewsRedisRepository: CakeViewsRedisRepository

	private fun initializeViews() {
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(1L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(1L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(1L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(1L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(2L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(2L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(2L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(3L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(3L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(4L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(5L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(6L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(7L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(7L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(1L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(7L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(8L)
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(9L)
	}

	@AfterEach
	fun cleanUp() {
		cakeViewsRedisRepository.clear()
	}

	@TestWithDisplayName("카테고리로 첫 페이지 케이크 이미지 조회에 성공한다")
	fun searchByCategory1() {
		// given
		val url = "$baseUrl/search/categories"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("category", CakeDesignCategory.FLOWER)
			.queryParam("pageSize", 5)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeImageWithShopInfoListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.lastCakeId shouldBe data.cakeImages.minOfOrNull { it.cakeId }
		data.size shouldBe 5
		data.cakeImages.forEach {
			val cakeCategory = cakeReadFacade.findCakeCategoryByCakeId(it.cakeId)
			cakeCategory.cakeDesignCategory shouldBe CakeDesignCategory.FLOWER
			it.shopName shouldBe "케이크 맛집" + it.cakeShopId
			it.thumbnailUrl shouldBe "thumbnail_url" + it.cakeShopId
		}
	}

	@TestWithDisplayName("카테고리로 첫 페이지가 아닌 케이크 이미지 조회에 성공한다")
	fun searchByCategory2() {
		// given
		val url = "$baseUrl/search/categories"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeId", 6)
			.queryParam("category", CakeDesignCategory.FLOWER)
			.queryParam("pageSize", 5)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeImageWithShopInfoListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.lastCakeId shouldBe data.cakeImages.minOfOrNull { it.cakeId }
		data.size shouldBe 5
		data.cakeImages.forEach {
			val cakeCategory = cakeReadFacade.findCakeCategoryByCakeId(it.cakeId)
			cakeCategory.cakeDesignCategory shouldBe CakeDesignCategory.FLOWER
			it.shopName shouldBe "케이크 맛집" + it.cakeShopId
			it.thumbnailUrl shouldBe "thumbnail_url" + it.cakeShopId
		}
	}

	@TestWithDisplayName("카테고리로 케이크 이미지 조회 시 데이터가 없으면 빈 배열을 반환한다")
	fun searchByCategory3() {
		// given
		val url = "$baseUrl/search/categories"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeId", 1)
			.queryParam("category", CakeDesignCategory.FLOWER)
			.queryParam("pageSize", 5)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeImageWithShopInfoListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeImages shouldHaveSize 0
		data.lastCakeId shouldBe null
		data.size shouldBe 0
	}

	@TestWithDisplayName("케이크 샵으로 첫 페이지 케이크 이미지 조회에 성공한다")
	fun searchByShopId1() {
		// given
		val url = "$baseUrl/search/shops"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeShopId", 1L)
			.queryParam("pageSize", 4)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeImageListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.lastCakeId shouldBe data.cakeImages.minOfOrNull { it.cakeId }
		data.size shouldBe 3
		data.cakeImages.forEach { it.cakeShopId shouldBe 1 }
	}

	@TestWithDisplayName("케이크 샵으로 첫 페이지가 아닌 케이크 이미지 조회에 성공한다")
	fun searchByShopId2() {
		// given
		val url = "$baseUrl/search/shops"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeId", 5)
			.queryParam("cakeShopId", 1L)
			.queryParam("pageSize", 4)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeImageListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.lastCakeId shouldBe data.cakeImages.minOfOrNull { it.cakeId }
		data.size shouldBe 3
		data.cakeImages.forEach { it.cakeShopId shouldBe 1 }
	}

	@TestWithDisplayName("케이크 샵으로 케이크 이미지 조회 시 데이터가 없으면 빈 배열을 반환한다")
	fun searchByShopId3() {
		// given
		val url = "$baseUrl/search/shops"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeId", 1)
			.queryParam("cakeShopId", 1L)
			.queryParam("pageSize", 4)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeImageListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeImages shouldHaveSize 0
		data.lastCakeId shouldBe null
		data.size shouldBe 0
	}

	@TestWithDisplayName("검색어, 태그명, 케이크 카테고리, 사용자 위치를 포함한 동적 검색에 성공한다")
	fun searchCakeImagesByKeywordAndLocation1() {
		val url = "$baseUrl/search/cakes"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("keyword", "tag")
			.queryParam("latitude", 37.2096575)
			.queryParam("longitude", 127.0998228)
			.queryParam("pageSize", 10)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeImageListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeImages.size shouldNotBeLessThan 0
	}

	@TestWithDisplayName("검색어, 태그명, 케이크 카테고리, 사용자 위치를 포함한 동적 검색에 성공한다")
	fun searchCakeImagesByKeywordAndLocation2() {
		val url = "$baseUrl/search/cakes"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("keyword", "tag")
			.queryParam("latitude", 37.543343)
			.queryParam("longitude", 127.052609)
			.queryParam("pageSize", 10)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeImageListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeImages.size shouldNotBeLessThan 0
	}

	@TestWithDisplayName("사용자 위치를 포함한 동적 검색에 성공한다")
	fun searchCakeImagesByKeywordAndLocation3() {
		val url = "$baseUrl/search/cakes"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("latitude", 37.2096575)
			.queryParam("longitude", 127.0998228)
			.queryParam("pageSize", 10)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeImageListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeImages.size shouldNotBeLessThan 0
	}

	@TestWithDisplayName("검색어와 커서 아이디로 동적 검색, 위치 정보 없이 4개가 조회된다")
	fun searchCakeImagesByKeyword() {
		val url = "$baseUrl/search/cakes"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeId", 5)
			.queryParam("keyword", "tag")
			.queryParam("pageSize", 10)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeImageListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeImages shouldHaveSize 4
	}

	@TestWithDisplayName("조회수로 케이크 이미지 조회에 성공한다")
	fun searchByViews1() {
		// given
		val url = "$baseUrl/search/views"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
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
		val data = objectMapper.convertValue(response.data, CakeImageListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeImages shouldHaveSize 4
	}

	@TestWithDisplayName("조회한 케이크가 없을 시, 인기 케이크 이미지 조회에 빈 배열을 리턴한다")
	fun searchByViews2() {
		// given
		val url = "$baseUrl/search/views"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("pageSize", 4)
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeImageListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeImages shouldHaveSize 0
	}

	@TestWithDisplayName("해당 id의 이전에 하트 누른 케이크에 대하여 하트 상태인지 조회에 성공한다.")
	fun isHeartCake1() {
		// given
		val cakeId = 3L
		val url = "$baseUrl/{cakeId}/heart"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
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

		data.isHeart shouldBe true
	}

	@TestWithDisplayName("해당 id의 이전에 하트 누르지 않은 케이크에 대하여 하트 상태인지 조회에 성공한다.")
	fun isHeartCake2() {
		// given
		val cakeId = 1L
		val url = "$baseUrl/{cakeId}/heart"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
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

	@TestWithDisplayName("해당 id의 케이크 하트에 성공한다.")
	fun heartCake() {
		// given
		val cakeId = 1L
		val url = "$baseUrl/{cakeId}/heart"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId)

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

	@TestWithDisplayName("해당 id의 케이크 하트 취소에 성공한다.")
	fun heartCancelCake() {
		// given
		val cakeId = 3L
		val url = "$baseUrl/{cakeId}/heart"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId)

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

	@TestWithDisplayName("케이크 정보 업데이트에 성공한다")
	fun updateCake() {
		// given
		val cakeId = 1L
		val url = "$baseUrl/{cakeId}"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId)
		val request = fixtureMonkey.giveMeBuilder(CakeUpdateRequest::class.java)
			.set("tagNames", listOf("tag_name1", "new_tag"))
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

		response.data shouldBe null
	}

	@TestWithDisplayName("어드민에 의해 케이크 정보 업데이트에 성공한다")
	fun updateCakeByAdmin() {
		// given
		val cakeId = 1L
		val url = "$baseUrl/{cakeId}"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId)
		val request = fixtureMonkey.giveMeBuilder(CakeUpdateRequest::class.java)
			.set("tagNames", listOf("tag_name1", "new_tag"))
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

		response.data shouldBe null
	}

	@TestWithDisplayName("케이크 삭제에 성공한다")
	fun deleteCake() {
		// given
		val cakeId = 1L
		val url = "$baseUrl/{cakeId}"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId)


		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.DELETE,
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

	@TestWithDisplayName("케이크 상세 조회에 성공한다")
	fun detailCake() {
		// given
		val cakeId = 1L
		val url = "$baseUrl/{cakeId}"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId)

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeDetailResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeImageUrl shouldBe "cake_image_url1"
		data.cakeShopName shouldBe "케이크 맛집1"
		data.shopBio shouldBe "케이크 맛집입니다."
		data.cakeShopId shouldBe 1L
		data.cakeCategories shouldHaveSize 1
		data.tags shouldHaveSize 5
	}

	@TestWithDisplayName("케이크 상세 조회에서 카테고리가 없는 경우, 태그가 없는 경우에 성공한다")
	fun detailCakeWithNoCategoriesAndNoTags() {
		// given
		val cakeId = 19L
		val url = "$baseUrl/{cakeId}"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId)

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeDetailResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeImageUrl shouldBe "cake_image_url19"
		data.cakeShopName shouldBe "케이크 맛집10"
		data.shopBio shouldBe "케이크 맛집입니다."
		data.cakeShopId shouldBe 10
		data.cakeCategories shouldHaveSize 0
		data.tags shouldHaveSize 0
	}

	@TestWithDisplayName("케이크 추가에 성공한다")
	fun createCake() {
		// given
		val cakeShopId = 1L
		val url = "$baseUrl/{cakeShopId}"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId)
		val request = fixtureMonkey.giveMeBuilder(CakeCreateRequest::class.java)
			.set("tagNames", listOf("tag_name1", "new_tag1", "new_tag2"))
			.sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.POST,
			HttpEntity(request, authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		response.data shouldBe null
	}
}

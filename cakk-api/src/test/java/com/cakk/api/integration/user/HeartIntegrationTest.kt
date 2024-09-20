package com.cakk.api.integration.user

import io.kotest.matchers.shouldBe

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
import com.cakk.api.dto.request.like.HeartCakeSearchRequest
import com.cakk.api.dto.request.like.HeartCakeShopSearchRequest
import com.cakk.api.dto.response.like.HeartCakeImageListResponse
import com.cakk.api.dto.response.like.HeartCakeShopListResponse
import com.cakk.common.enums.ReturnCode
import com.cakk.common.response.ApiResponse

@SqlGroup(
	Sql(
		scripts = ["/sql/insert-test-user.sql", "/sql/insert-cake.sql", "/sql/insert-heart.sql"],
		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
	),
	Sql(
		scripts = ["/sql/delete-all.sql"],
		executionPhase = ExecutionPhase.AFTER_TEST_METHOD
	)
)
class HeartIntegrationTest(
	@LocalServerPort private val port: Int
) : IntegrationTest() {

	protected val baseUrl = "$localhost$port/api/v1/me"

	@TestWithDisplayName("하트 한 케이크 목록을 조회한다.")
	fun listByHeart1() {
		// given
		val params = HeartCakeSearchRequest(null, 5)
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/heart-cakes")
			.queryParam("cakeHeartId", params.cakeHeartId)
			.queryParam("pageSize", params.pageSize)
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
		val data = objectMapper.convertValue(response.data, HeartCakeImageListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.lastCakeHeartId shouldBe data.cakeImages.minOfOrNull { it.cakeHeartId }
		data.size shouldBe params.pageSize
	}

	@TestWithDisplayName("하트 한 케이크 목록이 없을 시 빈 배열을 조회한다.")
	fun listByHeart2() {
		// given
		val params = HeartCakeSearchRequest(1L, 5)
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/heart-cakes")
			.queryParam("cakeHeartId", params.cakeHeartId)
			.queryParam("pageSize", params.pageSize)
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
		val data = objectMapper.convertValue(response.data, HeartCakeImageListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message


		data.lastCakeHeartId shouldBe data.cakeImages.minOfOrNull { it.cakeHeartId }
		data.size shouldBe 0
	}

	@TestWithDisplayName("하트 한 케이크 샵 목록을 조회한다.")
	fun listByHeartShops1() {
		// given
		val params = HeartCakeShopSearchRequest(null, 6)
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/heart-shops")
			.queryParam("cakeShopHeartId", params.cakeShopHeartId)
			.queryParam("pageSize", params.pageSize)
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
		val data = objectMapper.convertValue(response.data, HeartCakeShopListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.lastCakeShopHeartId shouldBe data.cakeShops.minOfOrNull { it.cakeShopHeartId }
		data.size shouldBe params.pageSize
	}

	@TestWithDisplayName("하트 한 케이크 샵 목록을 2개만 조회한다.")
	fun listByHeartShops2() {
		// given
		val params = HeartCakeShopSearchRequest(null, 2)
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/heart-shops")
			.queryParam("cakeShopHeartId", params.cakeShopHeartId)
			.queryParam("pageSize", params.pageSize)
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
		val data = objectMapper.convertValue(response.data, HeartCakeShopListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.lastCakeShopHeartId shouldBe data.cakeShops.minOfOrNull { it.cakeShopHeartId }
		data.size shouldBe params.pageSize
	}

	@TestWithDisplayName("하트 한 케이크 샵 목록이 없을 경우 빈 배열을 조회한다.")
	fun listByHeartShops3() {
		// given
		val params = HeartCakeShopSearchRequest(1L, 2)
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/heart-shops")
			.queryParam("cakeShopHeartId", params.cakeShopHeartId)
			.queryParam("pageSize", params.pageSize)
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
		val data = objectMapper.convertValue(response.data, HeartCakeShopListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.lastCakeShopHeartId shouldBe data.cakeShops.minOfOrNull { it.cakeShopHeartId }
		data.size shouldBe 0
	}
}

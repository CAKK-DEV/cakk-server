package com.cakk.api.integration.admin

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

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
import com.cakk.api.common.fixture.FixtureCommon.getDoubleFixtureBw
import com.cakk.api.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.api.dto.request.shop.CreateShopRequest
import com.cakk.api.dto.request.shop.PromotionRequest
import com.cakk.core.dto.response.shop.CakeShopCreateResponse
import com.cakk.core.dto.response.shop.CakeShopOwnerCandidateResponse
import com.cakk.core.dto.response.shop.CakeShopOwnerCandidatesResponse
import com.cakk.common.enums.Days
import com.cakk.common.enums.LinkKind
import com.cakk.common.enums.ReturnCode
import com.cakk.common.response.ApiResponse
import com.cakk.core.dto.param.shop.ShopLinkParam
import com.cakk.core.dto.param.shop.ShopOperationParam
import net.jqwik.api.Arbitraries
import java.time.LocalTime

@SqlGroup(
	Sql(
		scripts = ["/sql/insert-test-user.sql", "/sql/insert-business-information.sql"],
		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
	),
	Sql(
		scripts = ["/sql/delete-all.sql"],
		executionPhase = ExecutionPhase.AFTER_TEST_METHOD
	)
)
class AdminIntegrationTest(
	@LocalServerPort private val port: Int
) : IntegrationTest() {

	protected val baseUrl = "$localhost$port/api/v1/admin"

	@TestWithDisplayName("백 오피스 API, 케이크샵 생성에 성공한다")
	fun backOfficeCreateCakeShop() {
		val uriComponents = UriComponentsBuilder
			.fromUriString(baseUrl)
			.path("/shops/create")
			.build()

		val request = fixtureMonkey.giveMeBuilder(CreateShopRequest::class.java)
			.set("businessNumber", getStringFixtureBw(1, 20))
			.set("operationDays", listOf(ShopOperationParam(Days.MON, LocalTime.now(), LocalTime.now())))
			.set("shopName", getStringFixtureBw(1, 30))
			.set("shopBio", getStringFixtureBw(1, 30))
			.set("shopDescription", getStringFixtureBw(1, 30))
			.set("shopAddress", getStringFixtureBw(1, 30))
			.set("latitude", getDoubleFixtureBw(-90.0, 90.0))
			.set("longitude", getDoubleFixtureBw(-180.0, 180.0))
			.set("links", listOf(ShopLinkParam(LinkKind.WEB, "www.cake-shop.com")))
			.sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.POST,
			HttpEntity(request),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopCreateResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.cakeShopId shouldNotBe null
	}

	@TestWithDisplayName("백 오피스 API, 케이크샵 사장님 인증 요청 리스트 조회에 성공한다")
	fun backOfficeSearchByCakeShopBusinessOwnerCandidates() {
		//given
		val uriComponents = UriComponentsBuilder
			.fromUriString(baseUrl)
			.path("/shops/candidates")
			.build()

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopOwnerCandidatesResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data shouldNotBe null
	}

	@TestWithDisplayName("백 오피스 API, 케이크 샵 사장님 인증 완료 처리에 성공한다")
	fun backOfficeCakeShopBusinessOwnerApproved() {
		//given
		val uriComponents = UriComponentsBuilder
			.fromUriString(baseUrl)
			.path("/shops/promote")
			.build()

		val request = fixtureMonkey.giveMeBuilder(PromotionRequest::class.java)
			.set("userId", 1L)
			.set("cakeShopId", 1L)
			.sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity(request),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
	}

	@TestWithDisplayName("백 오피스 API, 케이크샵 사장님 인증 요청 상세 내용 조회에 성공한다")
	fun backOfficeSearchByCakeShopBusinessOwnerCandidate() {
		//given
		val userId = 1L
		val url = "$baseUrl/shops/candidates/{userId}"
		val uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(userId)

		// when
		val responseEntity = restTemplate.getForEntity(
			uriComponents.toUriString(),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, CakeShopOwnerCandidateResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.userId shouldBe 1L
		data.cakeShopId shouldBe 1L
		data.email shouldBe "test1@google.com"
		data.businessRegistrationImageUrl shouldBe "https://business_registration_image_url1"
		data.idCardImageUrl shouldBe "https://id_card_image_url1"
		data.emergencyContact shouldBe "010-0000-0000"
	}
}


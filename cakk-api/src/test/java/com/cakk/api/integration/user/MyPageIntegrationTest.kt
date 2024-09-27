package com.cakk.api.integration.user

import java.time.LocalDate

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import net.jqwik.api.Arbitraries

import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.web.util.UriComponentsBuilder

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.IntegrationTest
import com.cakk.api.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.api.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.api.dto.request.user.ProfileUpdateRequest
import com.cakk.core.dto.response.user.ProfileInformationResponse
import com.cakk.common.enums.Gender
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.common.response.ApiResponse

@SqlGroup(
	Sql(
		scripts = ["/sql/insert-test-user.sql", "/sql/insert-cake-shop.sql", "/sql/insert-heart.sql"],
		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
	),
	Sql(
		scripts = ["/sql/delete-all.sql"],
		executionPhase = ExecutionPhase.AFTER_TEST_METHOD
	)
)
internal class MyPageIntegrationTest(
	@LocalServerPort private val port: Int
) : IntegrationTest() {

	protected val baseUrl = "$localhost$port/api/v1/me"

	@TestWithDisplayName("프로필 조회에 성공한다.")
	fun profile() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
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
		val body = objectMapper.convertValue(response.data, ProfileInformationResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		val user = userReadFacade.findByUserId(userId)
		body.nickname shouldBe user.nickname
		body.profileImageUrl shouldBe user.profileImageUrl
		body.email shouldBe user.email
		body.gender shouldBe user.gender
		body.role shouldBe user.role
	}

	@TestWithDisplayName("토큰에 Bearer가 붙어있지 않으면 Filter에서 에러를 반환한다.")
	fun profile2() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.build()

		val headers = HttpHeaders()
		val jsonWebToken = authToken
		headers[HttpHeaders.AUTHORIZATION] = jsonWebToken.accessToken
		headers["Refresh"] = jsonWebToken.refreshToken

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			HttpEntity<Any>(headers),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(401)
		response.returnCode shouldBe ReturnCode.NOT_EXIST_BEARER_SUFFIX.code
		response.returnMessage shouldBe ReturnCode.NOT_EXIST_BEARER_SUFFIX.message
	}

	@TestWithDisplayName("프로필 수정에 성공한다.")
	fun modify() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.build()
		val body = fixtureMonkey.giveMeBuilder(ProfileUpdateRequest::class.java)
			.set("nickname", getStringFixtureBw(1, 20))
			.set("profileImageUrl", getStringFixtureBw(1, 200))
			.set("email", getStringFixtureBw(1, 50))
			.set("gender", Arbitraries.of(Gender::class.java))
			.set("birthday", LocalDate.now())
			.sample()

		// when
		val responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			HttpEntity(body, authHeader),
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		val user = userReadFacade.findByUserId(1L)
		body.nickname shouldBe user.nickname
		body.profileImageUrl shouldBe user.profileImageUrl
		body.email shouldBe user.email
		body.gender shouldBe user.gender
	}

	@TestWithDisplayName("회원 탈퇴에 성공한다.")
	fun withdraw() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.build()

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

		val exception = shouldThrow<CakkException> {
			userReadFacade.findByUserId(1L)
		}
		exception.getReturnCode() shouldBe ReturnCode.NOT_EXIST_USER
	}
}

package com.cakk.api.integration.user

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.web.util.UriComponentsBuilder

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.IntegrationTest
import com.cakk.core.dto.response.user.JwtResponse
import com.cakk.common.enums.ReturnCode
import com.cakk.common.response.ApiResponse
import com.cakk.domain.redis.repository.TokenRedisRepository

@SqlGroup(
	Sql(
		scripts = ["/sql/insert-test-user.sql"],
		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
	),
	Sql(
		scripts = ["/sql/delete-all.sql"],
		executionPhase = ExecutionPhase.AFTER_TEST_METHOD
	)
)
internal class SignIntegrationTest(
	@LocalServerPort private val port: Int
) : IntegrationTest() {

	protected val baseUrl = "$localhost$port/api/v1"

	@Autowired
	private lateinit var tokenRedisRepository: TokenRedisRepository

	@TestWithDisplayName("토큰 재발급에 성공한다.")
	fun recreate() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/recreate-token")
			.build()

		val jsonWebToken = authToken
		val headers = HttpHeaders()
		headers["Refresh"] = jsonWebToken.refreshToken

		val request = HttpEntity<Any>(headers)

		// when
		val responseEntity = restTemplate.postForEntity(
			uriComponents.toUriString(),
			request,
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
		val data = objectMapper.convertValue(response.data, JwtResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.accessToken shouldNotBe null
		data.refreshToken shouldNotBe null
		data.grantType shouldNotBe null
	}

	@TestWithDisplayName("비어있는 리프레시 토큰인 경우, 토큰 재발급에 실패한다.")
	fun recreate2() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/recreate-token")
			.build()

		val headers = HttpHeaders()
		headers["Refresh"] = ""

		val request = HttpEntity<Any>(headers)

		// when
		val responseEntity = restTemplate.postForEntity(
			uriComponents.toUriString(),
			request,
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(400)
		response.returnCode shouldBe ReturnCode.EMPTY_REFRESH.code
		response.returnMessage shouldBe ReturnCode.EMPTY_REFRESH.message
	}

	@TestWithDisplayName("블랙리스트인 리프레시 토큰인 경우, 토큰 재발급에 실패한다.")
	fun recreate3() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/recreate-token")
			.build()

		val jsonWebToken = authToken
		tokenRedisRepository.registerBlackList(jsonWebToken.refreshToken, 100000000)

		val headers = HttpHeaders()
		headers["Refresh"] = jsonWebToken.refreshToken

		val request: HttpEntity<*> = HttpEntity<Any?>(headers)

		// when
		val responseEntity = restTemplate.postForEntity(
			uriComponents.toUriString(),
			request,
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(400)
		response.returnCode shouldBe ReturnCode.BLACK_LIST_TOKEN.code
		response.returnMessage shouldBe ReturnCode.BLACK_LIST_TOKEN.message
	}

	@TestWithDisplayName("잘못된 리프레시 토큰인 경우, 토큰 재발급에 성공한다.")
	fun recreate4() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/recreate-token")
			.build()

		val refreshToken = "false token"
		val headers = HttpHeaders()
		headers["Refresh"] = refreshToken

		val request: HttpEntity<*> = HttpEntity<Any?>(headers)

		// when
		val responseEntity = restTemplate.postForEntity(
			uriComponents.toUriString(),
			request,
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(400)
		response.returnCode shouldBe ReturnCode.WRONG_JWT_TOKEN.code
		response.returnMessage shouldBe ReturnCode.WRONG_JWT_TOKEN.message
	}

	@TestWithDisplayName("로그아웃에 성공한다")
	fun signOut() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/sign-out")
			.build()

		val jsonWebToken = authToken
		tokenRedisRepository.deleteByToken(jsonWebToken.accessToken)
		tokenRedisRepository.deleteByToken(jsonWebToken.refreshToken)

		val headers = HttpHeaders()
		headers[HttpHeaders.AUTHORIZATION] = "Bearer " + jsonWebToken.accessToken
		headers["Refresh"] = jsonWebToken.refreshToken

		val request: HttpEntity<*> = HttpEntity<Any?>(headers)

		// when
		val responseEntity = restTemplate.postForEntity(
			uriComponents.toUriString(),
			request,
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message
	}

	@TestWithDisplayName("만료된 토큰일 경우 로그아웃에 실패한다")
	fun signOut2() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/sign-out")
			.build()

		val jsonWebToken = authToken
		tokenRedisRepository.registerBlackList(jsonWebToken.refreshToken, 100000000)

		val headers = HttpHeaders()
		headers[HttpHeaders.AUTHORIZATION] = "Bearer " + jsonWebToken.accessToken
		headers["Refresh"] = jsonWebToken.refreshToken

		val request = HttpEntity<Any>(headers)

		// when
		val responseEntity = restTemplate.postForEntity(
			uriComponents.toUriString(),
			request,
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(400)
		response.returnCode shouldBe ReturnCode.BLACK_LIST_TOKEN.code
		response.returnMessage shouldBe ReturnCode.BLACK_LIST_TOKEN.message
	}

	@TestWithDisplayName("잘못된 토큰일 경우 로그아웃에 실패한다")
	fun signOut3() {
		// given
		val uriComponents = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/sign-out")
			.build()

		val jsonWebToken = authToken

		val headers = HttpHeaders()
		headers[HttpHeaders.AUTHORIZATION] = "Bearer " + jsonWebToken.accessToken
		headers["Refresh"] = "wrong token"

		val request = HttpEntity<Any>(headers)

		// when
		val responseEntity = restTemplate.postForEntity(
			uriComponents.toUriString(),
			request,
			ApiResponse::class.java
		)

		// then
		val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(400)
		response.returnCode shouldBe ReturnCode.WRONG_JWT_TOKEN.code
		response.returnMessage shouldBe ReturnCode.WRONG_JWT_TOKEN.message
	}
}

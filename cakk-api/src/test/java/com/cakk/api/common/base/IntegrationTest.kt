package com.cakk.api.common.base

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

import com.cakk.core.vo.JsonWebToken
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.core.provider.jwt.JwtProvider
import com.cakk.domain.mysql.entity.user.User
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.*

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@SpringBootTest(properties = ["spring.profiles.active=test"], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class IntegrationTest {

	@Autowired
	protected lateinit var restTemplate: TestRestTemplate

	@Autowired
	protected lateinit var objectMapper: ObjectMapper

	@Autowired
	private lateinit var jwtProvider: JwtProvider

	@Autowired
	protected lateinit var userReadFacade: UserReadFacade

	@BeforeEach
	fun globalSetUp() {
		objectMapper = objectMapper.registerModule(KotlinModule.Builder()
			.withReflectionCacheSize(512)
			.configure(KotlinFeature.NullToEmptyCollection, false)
			.configure(KotlinFeature.NullToEmptyMap, false)
			.configure(KotlinFeature.NullIsSameAsDefault, false)
			.configure(KotlinFeature.SingletonSupport, false)
			.configure(KotlinFeature.StrictNullChecks, false)
			.build())
	}

	protected val authHeader: HttpHeaders
		get() {
			val headers = HttpHeaders()
			headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.accessToken)

			return headers
		}

	protected fun getAuthHeaderById(id: Long): HttpHeaders {
		val headers = HttpHeaders()
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthTokenById(id).accessToken)

		return headers
	}

	protected val authToken: JsonWebToken
		get() {
			val user: User = userReadFacade.findByUserId(1L)

			return jwtProvider.generateToken(user)
		}

	private fun getAuthTokenById(id: Long): JsonWebToken {
		val user: User = userReadFacade.findByUserId(id)

		return jwtProvider.generateToken(user)
	}

	protected val userId: Long
		get() {
			return 1L
		}

	protected final val localhost: String
		get() {
			return "http://localhost:"
		}
}

package com.cakk.api.common.base

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

import net.jqwik.api.Arbitraries

import com.cakk.api.provider.jwt.JwtProviderImpl
import com.cakk.core.vo.JsonWebToken
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.domain.mysql.entity.user.User
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.*
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@SpringBootTest(properties = ["spring.profiles.active=test"], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class IntegrationTest {

	@Autowired
	protected lateinit var restTemplate: TestRestTemplate

	@Autowired
	protected lateinit var objectMapper: ObjectMapper

	@Autowired
	private lateinit var jwtProviderImpl: JwtProviderImpl

	@Autowired
	protected lateinit var userReadFacade: UserReadFacade

	@BeforeEach
	fun globalSetUp() {
//		objectMapper = jsonMapper {
//			addModule(kotlinModule())
//			addModule(JavaTimeModule())
//		}
		objectMapper = objectMapper.registerModule(KotlinModule.Builder()
			.withReflectionCacheSize(512)
			.configure(KotlinFeature.NullToEmptyCollection, false)
			.configure(KotlinFeature.NullToEmptyMap, false)
			.configure(KotlinFeature.NullIsSameAsDefault, false)
			.configure(KotlinFeature.SingletonSupport, false)
			.configure(KotlinFeature.StrictNullChecks, false)
			.build())
	}

	protected fun getConstructorMonkey(): FixtureMonkey {
		return FixtureMonkey.builder()
			.plugin(JakartaValidationPlugin())
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.build()
	}

	protected fun getReflectionMonkey(): FixtureMonkey {
		return FixtureMonkey.builder()
			.plugin(JakartaValidationPlugin())
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.build()
	}

	protected fun getBuilderMonkey(): FixtureMonkey {
		return FixtureMonkey.builder()
			.plugin(JakartaValidationPlugin())
			.objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
			.build()
	}

	protected val authHeader: HttpHeaders
		get() {
			val headers: HttpHeaders = HttpHeaders()
			headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + authToken.accessToken)

			return headers
		}

	protected fun getAuthHeaderById(id: Long): HttpHeaders {
		val headers: HttpHeaders = HttpHeaders()
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthTokenById(id).accessToken)

		return headers
	}

	protected val authToken: JsonWebToken
		get() {
			val user: User = userReadFacade.findByUserId(1L)

			return jwtProviderImpl.generateToken(user)
		}

	private fun getAuthTokenById(id: Long): JsonWebToken {
		val user: User = userReadFacade.findByUserId(id)

		return jwtProviderImpl.generateToken(user)
	}

	protected val userId: Long
		get() {
			return 1L
		}

	protected fun getRandomAlpha(min: Int, max: Int): String {
		return Arbitraries.strings().alpha().withCharRange('a', 'z').ofMinLength(min).ofMaxLength(max).sample()
	}

	protected final val localhost: String
		get() {
			return "http://localhost:"
		}
}

package com.cakk.api.provider

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.test.util.ReflectionTestUtils

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.MockitoTest
import com.cakk.api.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.api.common.utils.*
import com.cakk.api.provider.jwt.JwtProviderImpl
import com.cakk.common.enums.Role
import com.cakk.core.vo.JsonWebToken
import com.cakk.infrastructure.cache.repository.TokenRedisRepository
import com.cakk.infrastructure.persistence.entity.user.UserEntity

@DisplayName("Jwt Provider 테스트")
internal class JwtProviderImplTest : MockitoTest() {

	private lateinit var jwtProviderImpl: JwtProviderImpl

	@Mock
	private lateinit var tokenRedisRepository: TokenRedisRepository

	@Value("\${jwt.secret}")
	private val secretKey: String = SECRET_KEY

	@BeforeEach
	fun setup() {
		jwtProviderImpl = JwtProviderImpl(
			Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)),
			tokenRedisRepository,
			ACCESS_TOKEN_EXPIRED_SECOND,
			REFRESH_TOKEN_EXPIRED_SECOND,
			GRANT_TYPE,
			USER_KEY
		)
	}

	@TestWithDisplayName("토큰 생성에 성공한다.")
	fun generateToken1() {
		// given
		val userEntity = fixtureMonkey.giveMeOne(UserEntity::class.java)

		// when
		val jwt: JsonWebToken = jwtProviderImpl.generateToken(userEntity)

		// then
		jwt.accessToken shouldNotBe null
		jwt.refreshToken shouldNotBe null
		jwt.grantType shouldNotBe null
	}

	@TestWithDisplayName("액세스 토큰으로부터 인증 정보를 가져온다.")
	fun getAuthentication() {
		// given
		val userEntity = fixtureMonkey.giveMeOne(UserEntity::class.java)
		ReflectionTestUtils.setField(userEntity, "id", 1L)
		ReflectionTestUtils.setField(userEntity, "role", Role.USER)

		val accessToken: String = jwtProviderImpl.generateToken(userEntity).accessToken

		// when
		val authentication: Authentication = jwtProviderImpl.getAuthentication(accessToken)

		// then
		authentication.authorities.toString() shouldBe "[${Role.USER.securityRole}]"
	}

	@TestWithDisplayName("토큰으로부터 Claim 정보를 가져온다.")
	fun parseClaims() {
		// given
		val userEntity = fixtureMonkey.giveMeOne(UserEntity::class.java)
		ReflectionTestUtils.setField(userEntity, "id", 1L)
		ReflectionTestUtils.setField(userEntity, "role", Role.USER)

		val accessToken: String = jwtProviderImpl.generateToken(userEntity).accessToken

		// when
		val claims = jwtProviderImpl.parseClaims(accessToken)

		// then
		claims shouldNotBe null
	}
}

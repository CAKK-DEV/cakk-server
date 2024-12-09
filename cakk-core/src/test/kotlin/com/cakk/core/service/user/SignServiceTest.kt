package com.cakk.core.service.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

import org.junit.jupiter.api.DisplayName
import org.mockito.ArgumentMatchers
import org.mockito.kotlin.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*

import com.cakk.common.enums.Gender
import com.cakk.common.enums.Provider
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.MockitoTest
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.core.common.fixture.FixtureCommon.getDateFixture
import com.cakk.core.common.fixture.FixtureCommon.getDateTimeFixture
import com.cakk.core.common.fixture.FixtureCommon.getEnumFixture
import com.cakk.core.common.fixture.FixtureCommon.getLongFixtureGoe
import com.cakk.core.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.core.common.fixture.FixtureCommon.getStringFixtureEq
import com.cakk.core.dispatcher.OidcProviderDispatcher
import com.cakk.core.dto.param.user.UserSignInParam
import com.cakk.core.dto.param.user.UserSignUpParam
import com.cakk.core.facade.user.UserManageFacade
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.core.provider.jwt.JwtProvider
import com.cakk.core.vo.JsonWebToken
import com.cakk.infrastructure.cache.repository.TokenRedisRepository

@DisplayName("Sign 관련 비즈니스 로직 테스트")
internal class SignServiceTest : MockitoTest() {

	@InjectMocks
	private lateinit var signService: SignService

	@Mock
	private lateinit var oidcProviderDispatcher: OidcProviderDispatcher

	@Mock
	private lateinit var jwtProvider: JwtProvider

	@Mock
	private lateinit var userReadFacade: UserReadFacade

	@Mock
	private lateinit var userManagerFacade: UserManageFacade

	@Mock
	private lateinit var tokenRedisRepository: TokenRedisRepository

	@TestWithDisplayName("회원가입에 성공한다")
	fun signUp1() {
		// given
		val param = fixtureMonkey.giveMeBuilder(UserSignUpParam::class.java)
			.set("provider", getEnumFixture(Provider::class.java))
			.set("idToken", getStringFixtureBw(100, 200))
			.set("nickname", getStringFixtureBw(1, 10))
			.set("email", getStringFixtureBw(10, 20))
			.set("birthday", getDateFixture())
			.set("gender", getEnumFixture(Gender::class.java))
			.setNull("deviceOs")
			.setNull("deviceToken")
			.sample()
		val user = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.entity.user.User::class.java)
			.set("id", getLongFixtureGoe(10))
			.set("provider", param.provider)
			.set("providerId", getStringFixtureBw(10, 20))
			.sample()
		val jwt = fixtureMonkey.giveMeBuilder(JsonWebToken::class.java)
			.set("accessToken", getStringFixtureBw(10, 20))
			.set("refreshToken", getStringFixtureBw(10, 20))
			.set("grantType", getStringFixtureBw(10, 20))
			.sample()

		doReturn(user.providerId).`when`(oidcProviderDispatcher).getProviderId(param.provider, param.idToken)
		doReturn(user).`when`(userManagerFacade).create(any())
		doReturn(jwt).`when`(jwtProvider).generateToken(user)

		// when
		val result = signService.signUp(param)

		// then
		result.accessToken shouldNotBe null
		result.refreshToken shouldNotBe null
		result.grantType shouldNotBe null

		verify(oidcProviderDispatcher, times(1)).getProviderId(param.provider, param.idToken)
		verify(userManagerFacade, times(1)).create(any())
		verify(jwtProvider, times(1)).generateToken(user)
	}

	@TestWithDisplayName("만료된 id token이라면 회원가입 시 에러를 던진다")
	fun signUp2() {
		// given
		val param: UserSignUpParam = fixtureMonkey.giveMeBuilder(UserSignUpParam::class.java)
			.setNotNull("provider")
			.setNotNull("idToken")
			.setNotNull("nickname")
			.setNotNull("email")
			.setNotNull("birthday")
			.setNotNull("gender")
			.sample()
		val user: com.cakk.infrastructure.persistence.entity.user.User = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.entity.user.User::class.java)
			.set("id", getLongFixtureGoe(10))
			.set("provider", param.provider)
			.set("providerId", getStringFixtureBw(10, 20))
			.sample()

		doThrow(CakkException(ReturnCode.EXPIRED_JWT_TOKEN)).`when`(oidcProviderDispatcher).getProviderId(param.provider, param.idToken)

		// when
		val exception = shouldThrow<CakkException> {
			signService.signUp(param)
		}

		//then
		exception.getReturnCode() shouldBe ReturnCode.EXPIRED_JWT_TOKEN

		verify(oidcProviderDispatcher, times(1)).getProviderId(param.provider, param.idToken)
		verify(userManagerFacade, times(0)).create(any())
		verify(jwtProvider, times(0)).generateToken(user)
	}

	@TestWithDisplayName("로그인에 성공한다.")
	fun signIn() {
		// given
		val param: UserSignInParam = getConstructorMonkey().giveMeBuilder(UserSignInParam::class.java)
			.setNotNull("provider")
			.setNotNull("idToken")
			.sample()
		val providerId: String = getStringFixtureEq(10).sample()
		val user: com.cakk.infrastructure.persistence.entity.user.User = getConstructorMonkey().giveMeBuilder(com.cakk.infrastructure.persistence.entity.user.User::class.java)
			.set("id", getLongFixtureGoe(10))
			.set("provider", param.provider)
			.set("providerId", providerId)
			.sample()
		val jwt: JsonWebToken = getConstructorMonkey().giveMeBuilder(JsonWebToken::class.java)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample()

		doReturn(providerId).`when`(oidcProviderDispatcher).getProviderId(param.provider, param.idToken)
		doReturn(user).`when`(userReadFacade).findByProviderId(providerId)
		doReturn(jwt).`when`(jwtProvider).generateToken(user)

		// when
		val result = signService.signIn(param)

		// then
		result.accessToken shouldNotBe null
		result.refreshToken shouldNotBe null
		result.grantType shouldNotBe null

		verify(oidcProviderDispatcher, times(1)).getProviderId(param.provider, param.idToken)
		verify(userReadFacade, times(1)).findByProviderId(providerId)
		verify(jwtProvider, times(1)).generateToken(user)
	}

	@TestWithDisplayName("제공자 id에 해당하는 유저가 없다면 로그인 시 에러를 던진다")
	fun signIn2() {
		// given
		val param: UserSignInParam = getConstructorMonkey().giveMeBuilder(UserSignInParam::class.java)
			.setNotNull("provider")
			.setNotNull("idToken")
			.sample()
		val providerId = getStringFixtureBw(10, 20).sample()
		val user: com.cakk.infrastructure.persistence.entity.user.User = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.entity.user.User::class.java)
			.set("id", getLongFixtureGoe(10))
			.set("provider", param.provider)
			.set("providerId", providerId)
			.sample()

		doReturn(providerId).`when`(oidcProviderDispatcher).getProviderId(param.provider, param.idToken)
		doThrow(CakkException(ReturnCode.NOT_EXIST_USER)).`when`(userReadFacade).findByProviderId(providerId)

		// when
		val exception = shouldThrow<CakkException> {
			signService.signIn(param)
		}

		//then
		exception.getReturnCode() shouldBe ReturnCode.NOT_EXIST_USER

		verify(oidcProviderDispatcher, times(1)).getProviderId(param.provider, param.idToken)
		verify(userReadFacade, times(1)).findByProviderId(providerId)
		verify(jwtProvider, times(0)).generateToken(user)
	}

	@TestWithDisplayName("토큰 재발급에 성공한다")
	fun recreateToken() {
		// given
		val refreshToken = "refresh token"
		val user: com.cakk.infrastructure.persistence.entity.user.User = fixtureMonkey.giveMeBuilder(com.cakk.infrastructure.persistence.entity.user.User::class.java)
			.set("id", getLongFixtureGoe(10))
			.set("providerId", getStringFixtureBw(10, 20))
			.set("createdAt", getDateTimeFixture())
			.set("updatedAt", getDateTimeFixture())
			.sample()
		val jwt: JsonWebToken = fixtureMonkey.giveMeBuilder(JsonWebToken::class.java)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample()

		doReturn(false).`when`(tokenRedisRepository).isBlackListToken(refreshToken)
		doReturn(11111L).`when`(jwtProvider).getTokenExpiredSecond(refreshToken)
		doReturn(user).`when`(jwtProvider).getUser(refreshToken)
		doReturn(jwt).`when`(jwtProvider).generateToken(user)

		// when
		val result = signService.recreateToken(refreshToken)

		// then
		result.accessToken shouldNotBe null
		result.refreshToken shouldNotBe null
		result.grantType shouldNotBe null

		verify(tokenRedisRepository, times(1)).isBlackListToken(refreshToken)
		verify(jwtProvider, times(1)).getUser(refreshToken)
		verify(jwtProvider, times(1)).generateToken(user)
	}

	@TestWithDisplayName("리프레시 토큰이 만료된 경우, 토큰 재발급에 실패한다")
	fun recreateToken2() {
		// given
		val refreshToken = "refresh token"

		doReturn(false).`when`(tokenRedisRepository).isBlackListToken(refreshToken)
		doThrow(CakkException(ReturnCode.EXPIRED_JWT_TOKEN)).`when`(jwtProvider).getUser(refreshToken)

		// when
		val exception = shouldThrow<CakkException> {
			signService.recreateToken(refreshToken)
		}

		//then
		exception.getReturnCode() shouldBe ReturnCode.EXPIRED_JWT_TOKEN

		verify(jwtProvider, times(1)).getUser(refreshToken)
		verify(jwtProvider, times(0)).generateToken(any())
	}

	@TestWithDisplayName("리프레시 토큰에 null인 유저가 담겨있는 경우, 토큰 재발급에 실패한다")
	fun recreateToken3() {
		// given
		val refreshToken = "refresh token"

		doReturn(false).`when`(tokenRedisRepository).isBlackListToken(refreshToken)
		doThrow(CakkException(ReturnCode.EMPTY_USER)).`when`(jwtProvider).getUser(refreshToken)

		// when
		val exception = shouldThrow<CakkException> {
			signService.recreateToken(refreshToken)
		}

		//then
		exception.getReturnCode() shouldBe ReturnCode.EMPTY_USER

		verify(jwtProvider, times(1)).getUser(refreshToken)
		verify(jwtProvider, times(0)).generateToken(any())
	}

	@TestWithDisplayName("리프레시 토큰이 블랙리스트에 있는 경우, 토큰 재발급에 실패한다")
	fun recreateToken4() {
		// given
		val refreshToken = "refresh token"

		doReturn(true).`when`(tokenRedisRepository).isBlackListToken(refreshToken)

		// when
		val exception = shouldThrow<CakkException> {
			signService.recreateToken(refreshToken)
		}

		//then
		exception.getReturnCode() shouldBe ReturnCode.BLACK_LIST_TOKEN

		verify(tokenRedisRepository, times(1)).isBlackListToken(refreshToken)
		verify(jwtProvider, times(0)).getUser(refreshToken)
	}

	@TestWithDisplayName("로그아웃에 성공한다")
	fun signOut() {
		// given
		val accessToken: String = getStringFixtureBw(10, 20).sample()
		val refreshToken: String = getStringFixtureBw(10, 20).sample()

		doReturn(11111111111111L).`when`(jwtProvider).getTokenExpiredSecond(accessToken)
		doReturn(11111111111111L).`when`(jwtProvider).getTokenExpiredSecond(refreshToken)
		doNothing().`when`(tokenRedisRepository).registerBlackList(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())

		// when
		signService.signOut(accessToken, refreshToken)

		// then
		verify(jwtProvider, times(2)).getTokenExpiredSecond(ArgumentMatchers.anyString())
		verify(tokenRedisRepository, times(2)).registerBlackList(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())
	}

	@TestWithDisplayName("만료된 액세스 토큰일 경우 로그아웃에 실패한다")
	fun signOut2() {
		// given
		val accessToken: String = getStringFixtureBw(10, 20).sample()

		doThrow(CakkException(ReturnCode.EXPIRED_JWT_TOKEN)).`when`(jwtProvider).getTokenExpiredSecond(accessToken)

		// when
		val exception = shouldThrow<CakkException> {
			signService.signOut(accessToken, "")
		}

		//then
		exception.getReturnCode() shouldBe ReturnCode.EXPIRED_JWT_TOKEN

		verify(jwtProvider, times(1)).getTokenExpiredSecond(anyString())
		verify(tokenRedisRepository, times(0)).registerBlackList(anyString(), anyLong())
	}

	@TestWithDisplayName("만료된 리프레시 토큰일 경우 로그아웃에 실패한다")
	fun signOut3() {
		// given
		val accessToken: String = getStringFixtureBw(10, 20).sample()
		val refreshToken: String = getStringFixtureBw(10, 20).sample()

		doReturn(11111111111111L).`when`(jwtProvider).getTokenExpiredSecond(accessToken)
		doThrow(CakkException(ReturnCode.EXPIRED_JWT_TOKEN)).`when`(jwtProvider).getTokenExpiredSecond(refreshToken)

		// when
		val exception = shouldThrow<CakkException> {
			signService.signOut(accessToken, refreshToken)
		}

		//then
		exception.getReturnCode() shouldBe ReturnCode.EXPIRED_JWT_TOKEN

		verify(jwtProvider, times(2)).getTokenExpiredSecond(ArgumentMatchers.anyString())
		verify(tokenRedisRepository, times(0)).registerBlackList(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())
	}

	@TestWithDisplayName("유효하지 않은 액세스 토큰일 경우 로그아웃에 실패한다")
	fun signOut4() {
		// given
		val accessToken: String = getStringFixtureBw(10, 20).sample()

		doThrow(CakkException(ReturnCode.WRONG_JWT_TOKEN)).`when`(jwtProvider).getTokenExpiredSecond(accessToken)

		// when
		val exception = shouldThrow<CakkException> {
			signService.signOut(accessToken, "")
		}

		//then
		exception.getReturnCode() shouldBe ReturnCode.WRONG_JWT_TOKEN

		verify(jwtProvider, times(1)).getTokenExpiredSecond(anyString())
		verify(tokenRedisRepository, times(0)).registerBlackList(anyString(), anyLong())
	}

	@TestWithDisplayName("유효하지 않은 리프레시 토큰일 경우 로그아웃에 실패한다")
	fun signOut5() {
		// given
		val accessToken: String = getStringFixtureBw(10, 20).sample()
		val refreshToken: String = getStringFixtureBw(10, 20).sample()

		doReturn(11111111111111L).`when`(jwtProvider).getTokenExpiredSecond(accessToken)
		doThrow(CakkException(ReturnCode.WRONG_JWT_TOKEN)).`when`(jwtProvider).getTokenExpiredSecond(refreshToken)

		// when
		val exception = shouldThrow<CakkException> {
			signService.signOut(accessToken, refreshToken)
		}

		//then
		exception.getReturnCode() shouldBe ReturnCode.WRONG_JWT_TOKEN

		verify(jwtProvider, times(2)).getTokenExpiredSecond(anyString())
		verify(tokenRedisRepository, times(0)).registerBlackList(anyString(), anyLong())
	}
}

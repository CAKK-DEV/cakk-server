package com.cakk.core.service.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.context.ApplicationEventPublisher

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.MockitoTest
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.core.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.core.common.fixture.FixtureCommon.getStringFixtureEq
import com.cakk.core.dto.param.user.GenerateCodeParam
import com.cakk.core.dto.param.user.VerifyEmailParam
import com.cakk.domain.redis.repository.EmailVerificationRedisRepository

class EmailVerificationServiceTest : MockitoTest() {

	@InjectMocks
	private lateinit var emailVerificationService: EmailVerificationService

	@Mock
	private lateinit var emailVerificationRedisRepository: EmailVerificationRedisRepository

	@Mock
	private lateinit var applicationEventPublisher: ApplicationEventPublisher

	@TestWithDisplayName("이메일 인증 코드를 생성하고 메일을 전송한다.")
	fun sendEmailForVerification() {
		// given
		val dto = fixtureMonkey.giveMeBuilder(GenerateCodeParam::class.java)
			.set("email", getStringFixtureBw(5, 10))
			.sample()

		// when
		emailVerificationService.sendEmailForVerification(dto)

		// then
		verify(emailVerificationRedisRepository, times(1)).save(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())
	}

	@TestWithDisplayName("이메일 인증 코드를 검증한다.")
	fun checkEmailVerificationCode() {
		// given
		val dto = fixtureMonkey.giveMeBuilder(VerifyEmailParam::class.java)
			.set("email", getStringFixtureBw(5, 10))
			.set("code", getStringFixtureEq(6))
			.sample()

		doReturn(dto.code).`when`(emailVerificationRedisRepository).findCodeByEmail(dto.email)

		// when
		emailVerificationService.checkEmailVerificationCode(dto)

		// then
		verify(emailVerificationRedisRepository, times(1)).findCodeByEmail(dto.email)
	}

	@TestWithDisplayName("이메일 인증 코드가 올바르지 않으면 검증 시 에러를 반환한다.")
	fun checkEmailVerificationCode2() {
		// given
		val dto = fixtureMonkey.giveMeBuilder(VerifyEmailParam::class.java)
			.set("email", getStringFixtureBw(5, 10))
			.set("code", getStringFixtureEq(6))
			.sample()

		doReturn("올바른인증번호").`when`(emailVerificationRedisRepository).findCodeByEmail(dto.email)

		// when
		val exception = shouldThrow<CakkException> {
			emailVerificationService.checkEmailVerificationCode(dto)
		}

		// then
		exception.getReturnCode() shouldBe ReturnCode.WRONG_VERIFICATION_CODE
		verify(emailVerificationRedisRepository, times(1)).findCodeByEmail(dto.email)
	}
}

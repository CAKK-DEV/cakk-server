package com.cakk.api.integration.aspect

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import org.aspectj.lang.ProceedingJoinPoint
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.aspect.AopForTransaction

@ActiveProfiles("test")
@SpringBootTest(properties = ["spring.profiles.active=test"])
class AopForTransactionTest {

	@Autowired
	private lateinit var aopForTransaction: AopForTransaction

	@TestWithDisplayName("proceed 메소드가 정상적으로 실행된다.")
	fun proceed() {
		// given
		val joinPoint = mock(ProceedingJoinPoint::class.java)

		doReturn(Any::class.java).`when`(joinPoint).proceed()

		// when
		aopForTransaction.proceed(joinPoint)

		// then
		verify(joinPoint, times(1)).proceed()
	}

	@TestWithDisplayName("proceed 메소드 실행 시 CakkException 에러가 터지면 CakkException 에러를 던진다.")
	@Throws(
		Throwable::class
	)
	fun proceed2() {
		// given
		val joinPoint = mock(ProceedingJoinPoint::class.java)

		doThrow(CakkException(ReturnCode.INTERNAL_SERVER_ERROR)).`when`(joinPoint).proceed()

		// when & then
		val exception = shouldThrow<CakkException> {
			aopForTransaction.proceed(joinPoint)
		}

		exception.getReturnCode() shouldBe ReturnCode.INTERNAL_SERVER_ERROR
		verify(joinPoint, times(1)).proceed()
	}

	@TestWithDisplayName("proceed 메소드 실행 시 다른 에러가 터지면 RuntimeException 에러를 던진다.")
	@Throws(Throwable::class)
	fun proceed3() {
		// given
		val joinPoint = Mockito.mock(ProceedingJoinPoint::class.java)

		doThrow(RuntimeException()).`when`(joinPoint).proceed()

		// when & then
		shouldThrow<RuntimeException> {
			aopForTransaction.proceed(joinPoint)
		}

		verify(joinPoint, times(1)).proceed()
	}
}

package com.cakk.api.validator

import jakarta.validation.ConstraintValidatorContext

import io.kotest.matchers.shouldBe

import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.Mockito.mock

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.MockitoTest
import com.cakk.api.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.common.enums.Days
import com.cakk.core.dto.param.shop.ShopOperationParam

internal class OperationValidatorTest : MockitoTest() {

	@InjectMocks
	private lateinit var operationValidator: OperationValidator

	@TestWithDisplayName("validation 체크에 통과하면 true를 반환한다.")
	fun isValid1() {
		// given
		val params = fixtureMonkey.giveMeBuilder(ShopOperationParam::class.java)
			.setNotNull("operationDay")
			.setNotNull("operationStartTime")
			.setNotNull("operationEndTime")
			.sampleList(1)

		val context = mock(ConstraintValidatorContext::class.java)

		// when
		val result = operationValidator.isValid(params, context)

		// then
		result shouldBe true
	}


	@TestWithDisplayName("같은 요일이 여러개 담겨있으면 false를 반환한다.")
	fun isValid2() {
		// given
		val params = fixtureMonkey.giveMeBuilder(ShopOperationParam::class.java)
			.set("operationDay", Days.MON)
			.setNotNull("operationStartTime")
			.setNotNull("operationEndTime")
			.sampleList(3)
		val context = mock(ConstraintValidatorContext::class.java)

		// when
		val result = operationValidator.isValid(params, context)

		// then
		result shouldBe false
	}

	@TestWithDisplayName("operationParams가 null이면 false를 반환한다.")
	fun isValid3() {
		// given
		val context = mock(ConstraintValidatorContext::class.java)

		// when
		val result = operationValidator.isValid(null, context)

		// then
		result shouldBe false
	}
}

package com.cakk.api.validator;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.List;

import jakarta.validation.ConstraintValidatorContext;

import org.mockito.InjectMocks;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockitoTest;
import com.cakk.core.dto.param.shop.ShopOperationParam;
import com.cakk.common.enums.Days;

public class OperationValidatorTest extends MockitoTest {

	@InjectMocks
	private OperationValidator operationValidator;

	@TestWithDisplayName("validation 체크에 통과하면 true를 반환한다.")
	void isValid1() {
		// given
		final List<ShopOperationParam> params = getConstructorMonkey().giveMeBuilder(ShopOperationParam.class)
			.set("operationDay", Arbitraries.of(Days.class).sample())
			.set("openTime", Arbitraries.of(LocalTime.class).sample())
			.set("closeTime", Arbitraries.of(LocalTime.class).sample())
			.sampleList(1);
		final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

		// when
		boolean result = operationValidator.isValid(params, context);

		// then
		assertThat(result).isTrue();
	}


	@TestWithDisplayName("같은 요일이 여러개 담겨있으면 false를 반환한다.")
	void isValid2() {
		// given
		final List<ShopOperationParam> params = getConstructorMonkey().giveMeBuilder(ShopOperationParam.class)
			.set("operationDay", Days.MON)
			.set("openTime", Arbitraries.of(LocalTime.class).sample())
			.set("closeTime", Arbitraries.of(LocalTime.class).sample())
			.sampleList(3);
		final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

		// when
		boolean result = operationValidator.isValid(params, context);

		// then
		assertThat(result).isFalse();
	}

	@TestWithDisplayName("operationParams가 null이면 false를 반환한다.")
	void isValid3() {
		// given
		final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

		// when
		boolean result = operationValidator.isValid(null, context);

		// then
		assertThat(result).isFalse();
	}
}

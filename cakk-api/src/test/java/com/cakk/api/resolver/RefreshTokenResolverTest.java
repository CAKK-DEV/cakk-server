package com.cakk.api.resolver;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.springframework.core.MethodParameter;

import com.cakk.api.annotation.RefreshToken;
import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockitoTest;

class RefreshTokenResolverTest extends MockitoTest {

	@InjectMocks
	private RefreshTokenResolver refreshTokenResolver;

	@TestWithDisplayName("supportsParameter 메서드는 RefreshToken 어노테이션이 붙은 String 타입의 파라미터를 지원한다.")
	void supportsParameter() {
		// given
		MethodParameter parameter = mock(MethodParameter.class);

		doReturn(true).when(parameter).hasParameterAnnotation(RefreshToken.class);
		doReturn(String.class).when(parameter).getParameterType();

		// when
		boolean result = refreshTokenResolver.supportsParameter(parameter);

		// then
		assertThat(result).isTrue();
	}

	@TestWithDisplayName("String 타입이 아닌 경우, false를 반환한다.")
	void supportsParameter2() {
		// given
		MethodParameter parameter = mock(MethodParameter.class);

		doReturn(true).when(parameter).hasParameterAnnotation(RefreshToken.class);
		doReturn(Integer.class).when(parameter).getParameterType();

		// when
		boolean result = refreshTokenResolver.supportsParameter(parameter);

		// then
		assertThat(result).isFalse();
	}
}

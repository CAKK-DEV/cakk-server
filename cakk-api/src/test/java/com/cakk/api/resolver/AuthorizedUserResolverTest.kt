package com.cakk.api.resolver;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.springframework.core.MethodParameter;

import com.cakk.api.annotation.SignInUser;
import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockitoTest;
import com.cakk.domain.mysql.entity.user.User;

class AuthorizedUserResolverTest extends MockitoTest {

	@InjectMocks
	private AuthorizedUserResolver authorizedUserResolver;

	@TestWithDisplayName("supportsParameter 메서드는 RefreshToken 어노테이션이 붙은 String 타입의 파라미터를 지원한다.")
	void supportsParameter() {
		// given
		MethodParameter parameter = mock(MethodParameter.class);

		doReturn(true).when(parameter).hasParameterAnnotation(SignInUser.class);
		doReturn(User.class).when(parameter).getParameterType();

		// when
		boolean result = authorizedUserResolver.supportsParameter(parameter);

		// then
		assertThat(result).isTrue();
	}

	@TestWithDisplayName("String 타입이 아닌 경우, false를 반환한다.")
	void supportsParameter2() {
		// given
		MethodParameter parameter = mock(MethodParameter.class);

		doReturn(true).when(parameter).hasParameterAnnotation(SignInUser.class);
		doReturn(Integer.class).when(parameter).getParameterType();

		// when
		boolean result = authorizedUserResolver.supportsParameter(parameter);

		// then
		assertThat(result).isFalse();
	}
}

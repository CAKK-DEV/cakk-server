package com.cakk.api.resolver;

import static org.springframework.util.StringUtils.*;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.cakk.api.annotation.AccessToken;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

public class AccessTokenResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AccessToken.class)
			&& String.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public String resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		String accessToken = webRequest.getHeader("Authorization");

		if (!hasLength(accessToken)) {
			throw new CakkException(ReturnCode.EMPTY_ACCESS);
		}
		accessToken = accessToken.replace("Bearer ", "");

		return accessToken;
	}
}

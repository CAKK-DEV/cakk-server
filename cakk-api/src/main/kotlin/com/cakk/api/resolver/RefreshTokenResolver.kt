package com.cakk.api.resolver

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

import com.cakk.api.annotation.RefreshToken
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException

class RefreshTokenResolver : HandlerMethodArgumentResolver {

	override fun supportsParameter(parameter: MethodParameter): Boolean {
		return parameter.hasParameterAnnotation(RefreshToken::class.java)
			&& String::class.java.isAssignableFrom(parameter.parameterType)
	}

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?
	): Any {
		val refreshToken = webRequest.getHeader("Refresh")

		if (refreshToken.isNullOrBlank()) {
			throw CakkException(ReturnCode.EMPTY_REFRESH)
		}

		return refreshToken
	}
}
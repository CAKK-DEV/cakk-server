package com.cakk.api.resolver

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

import com.cakk.api.annotation.AccessToken
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException


class AccessTokenResolver : HandlerMethodArgumentResolver {

	override fun supportsParameter(parameter: MethodParameter): Boolean {
		return parameter.hasParameterAnnotation(AccessToken::class.java)
			&& String::class.java.isAssignableFrom(parameter.parameterType)
	}

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?
	): Any {
		val accessToken = webRequest.getHeader("Authorization")

		if (accessToken.isNullOrBlank()) {
			throw CakkException(ReturnCode.EMPTY_ACCESS)
		}

		return accessToken.replace("Bearer ", "")
	}
}

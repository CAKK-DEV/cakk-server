package com.cakk.api.resolver

import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

import com.cakk.api.annotation.SignInUser
import com.cakk.api.vo.OAuthUserDetails
import com.cakk.infrastructure.persistence.entity.user.User

class AuthorizedUserResolver : HandlerMethodArgumentResolver {
	override fun supportsParameter(parameter: MethodParameter): Boolean {
		return parameter.hasParameterAnnotation(SignInUser::class.java)
			&& com.cakk.infrastructure.persistence.entity.user.User::class.java.isAssignableFrom(parameter.parameterType)
	}

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?
	): Any? {
		val userDetails = SecurityContextHolder.getContext().authentication.principal as OAuthUserDetails?

		return userDetails?.getUser()
	}
}

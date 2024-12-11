package com.cakk.admin.resolver

import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

import com.cakk.admin.annotation.AdminUser
import com.cakk.admin.vo.OAuthUserDetails

import com.cakk.infrastructure.persistence.entity.user.UserEntity

class AdminUserResolver : HandlerMethodArgumentResolver {
	override fun supportsParameter(parameter: MethodParameter): Boolean {
		return parameter.hasParameterAnnotation(AdminUser::class.java)
			&& UserEntity::class.java.isAssignableFrom(parameter.parameterType)
	}

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?
	): Any {
		val userDetails = SecurityContextHolder.getContext().authentication.principal as OAuthUserDetails

		return userDetails.getUser()
	}
}

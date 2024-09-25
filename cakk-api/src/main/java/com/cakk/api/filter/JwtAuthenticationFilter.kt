package com.cakk.api.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

import com.cakk.api.provider.jwt.JwtProviderImpl
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException

@Component
class JwtAuthenticationFilter(
	private val jwtProviderImpl: JwtProviderImpl,
	@Value("\${jwt.access-header}")
	private val accessHeader: String,
	@Value("\${jwt.grant-type}")
	private val grantType: String
) : OncePerRequestFilter() {

	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
		val token = getTokensFromHeader(request, accessHeader)

		token?.let {
			val accessToken = replaceBearerToBlank(it)
			val authentication = jwtProviderImpl.getAuthentication(accessToken)

			SecurityContextHolder.getContext().authentication = authentication
		}

		filterChain.doFilter(request, response)
	}

	private fun getTokensFromHeader(request: HttpServletRequest, header: String): String? {
		return request.getHeader(header)
	}

	private fun replaceBearerToBlank(token: String): String {
		val suffix = "$grantType "

		if (!token.startsWith(suffix)) {
			throw CakkException(ReturnCode.NOT_EXIST_BEARER_SUFFIX)
		}

		return token.replace(suffix, "")
	}
}

package com.cakk.api.filter;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cakk.api.provider.jwt.JwtProviderImpl;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProviderImpl jwtProviderImpl;
	private final String accessHeader;
	private final String grantType;

	public JwtAuthenticationFilter(
		JwtProviderImpl jwtProviderImpl,

		@Value("${jwt.access-header}") String accessHeader,

		@Value("${jwt.grant-type}") String grantType
	) {
		this.jwtProviderImpl = jwtProviderImpl;
		this.accessHeader = accessHeader;
		this.grantType = grantType;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
		ServletException,
		IOException {
		Optional<String> token = getTokensFromHeader(request, accessHeader);

		token.ifPresent(it -> {
			String accessToken = replaceBearerToBlank(it);

			Authentication authentication = jwtProviderImpl.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		});

		filterChain.doFilter(request, response);
	}

	private Optional<String> getTokensFromHeader(HttpServletRequest request, String header) {
		return Optional.ofNullable(request.getHeader(header));
	}

	private String replaceBearerToBlank(String token) {
		String suffix = grantType + " ";

		if (!token.startsWith(suffix)) {
			throw new CakkException(ReturnCode.NOT_EXIST_BEARER_SUFFIX);
		}

		return token.replace(suffix, "");
	}
}

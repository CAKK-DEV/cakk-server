package com.cakk.api.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.common.response.ApiResponse;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
		ServletException,
		IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (CakkException exception) {
			setErrorResponse(exception.getReturnCode(), response);
		}
	}

	private void setErrorResponse(ReturnCode returnCode, HttpServletResponse response) {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json; charset=UTF-8");
		ApiResponse<String> result = ApiResponse.fail(returnCode);

		try {
			response.getWriter().write(toJson(result));
		} catch (IOException e) {
			// ignored
		}
	}

	private String toJson(Object data) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(data);
	}
}

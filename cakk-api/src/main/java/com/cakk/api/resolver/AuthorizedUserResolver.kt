package com.cakk.api.resolver;

import static java.util.Objects.*;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.cakk.api.annotation.SignInUser;
import com.cakk.api.vo.OAuthUserDetails;
import com.cakk.domain.mysql.entity.user.User;

public class AuthorizedUserResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(SignInUser.class)
			&& User.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public User resolveArgument(MethodParameter parameter,
								ModelAndViewContainer mavContainer,
								NativeWebRequest webRequest,
								WebDataBinderFactory binderFactory) throws Exception {
		final OAuthUserDetails userDetails = (OAuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return isNull(userDetails) ? null : userDetails.getUser();
	}
}

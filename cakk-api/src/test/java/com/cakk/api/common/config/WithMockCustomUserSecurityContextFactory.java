package com.cakk.api.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import com.cakk.api.common.annotation.MockCustomUser;
import com.cakk.api.vo.OAuthUserDetails;
import com.cakk.domain.entity.user.User;
import com.cakk.domain.repository.reader.UserReader;

@Component
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<MockCustomUser> {

	@Autowired
	private UserReader userReader;

	@Override
	public SecurityContext createSecurityContext(MockCustomUser annotation) {
		final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

		final User user = userReader.findByUserId(1L);
		final OAuthUserDetails userDetails = new OAuthUserDetails(user);
		final UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		securityContext.setAuthentication(authenticationToken);
		return securityContext;
	}
}

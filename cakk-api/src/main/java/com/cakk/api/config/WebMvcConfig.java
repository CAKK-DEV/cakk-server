package com.cakk.api.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cakk.api.resolver.AccessTokenResolver;
import com.cakk.api.resolver.AuthorizedUserResolver;
import com.cakk.api.resolver.RefreshTokenResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AuthorizedUserResolver());
		resolvers.add(new AccessTokenResolver());
		resolvers.add(new RefreshTokenResolver());
	}
}

package com.cakk.api.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

import com.cakk.api.resolver.AccessTokenResolver
import com.cakk.api.resolver.AuthorizedUserResolver
import com.cakk.api.resolver.RefreshTokenResolver

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(AuthorizedUserResolver())
        resolvers.add(AccessTokenResolver())
        resolvers.add(RefreshTokenResolver())
    }
}

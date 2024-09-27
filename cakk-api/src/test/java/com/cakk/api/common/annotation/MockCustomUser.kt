package com.cakk.api.common.annotation

import com.cakk.api.common.config.WithMockCustomUserSecurityContextFactory
import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class MockCustomUser

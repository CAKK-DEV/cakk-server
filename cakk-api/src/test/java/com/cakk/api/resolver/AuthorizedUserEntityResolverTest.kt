package com.cakk.api.resolver

import org.mockito.InjectMocks
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.springframework.core.MethodParameter

import io.kotest.matchers.shouldBe

import com.cakk.api.annotation.SignInUser
import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.MockitoTest
import com.cakk.infrastructure.persistence.entity.user.UserEntity

internal class AuthorizedUserEntityResolverTest : MockitoTest() {

    @InjectMocks
    private lateinit var authorizedUserResolver: AuthorizedUserResolver

    @TestWithDisplayName("supportsParameter 메서드는 RefreshToken 어노테이션이 붙은 String 타입의 파라미터를 지원한다.")
    fun supportsParameter() {
        // given
        val parameter = mock(MethodParameter::class.java)

        doReturn(true).`when`(parameter).hasParameterAnnotation(SignInUser::class.java)
        doReturn(UserEntity::class.java).`when`(parameter).parameterType

        // when
        val result = authorizedUserResolver.supportsParameter(parameter)

        // then
		result shouldBe true
    }

    @TestWithDisplayName("String 타입이 아닌 경우, false를 반환한다.")
    fun supportsParameter2() {
        // given
        val parameter = mock(MethodParameter::class.java)

        doReturn(true).`when`(parameter).hasParameterAnnotation(SignInUser::class.java)
        doReturn(Int::class.java).`when`(parameter).parameterType

        // when
        val result = authorizedUserResolver.supportsParameter(parameter)

        // then
		result shouldBe false
    }
}
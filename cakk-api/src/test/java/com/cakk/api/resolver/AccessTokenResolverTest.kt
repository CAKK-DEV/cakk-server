package com.cakk.api.resolver

import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.springframework.core.MethodParameter

import io.kotest.matchers.shouldBe

import com.cakk.api.annotation.AccessToken
import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.MockitoTest

internal class AccessTokenResolverTest : MockitoTest() {

    @InjectMocks
    private lateinit var accessTokenResolver: AccessTokenResolver

    @TestWithDisplayName("supportsParameter 메서드는 RefreshToken 어노테이션이 붙은 String 타입의 파라미터를 지원한다.")
    fun supportsParameter() {
        // given
        val parameter = Mockito.mock(MethodParameter::class.java)

        doReturn(true).`when`(parameter).hasParameterAnnotation(AccessToken::class.java)
        doReturn(String::class.java).`when`(parameter).parameterType

        // when
        val result = accessTokenResolver.supportsParameter(parameter)

        // then
		result shouldBe true
    }

    @TestWithDisplayName("String 타입이 아닌 경우, false를 반환한다.")
    fun supportsParameter2() {
        // given
        val parameter = Mockito.mock(MethodParameter::class.java)

        doReturn(true).`when`(parameter).hasParameterAnnotation(AccessToken::class.java)
        doReturn(Int::class.java).`when`(parameter).parameterType

        // when
        val result = accessTokenResolver.supportsParameter(parameter)

        // then
		result shouldBe false
    }
}

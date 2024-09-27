package com.cakk.api.dispatcher

import java.util.*

import org.springframework.stereotype.Component

import com.cakk.core.provider.oauth.OidcProvider
import com.cakk.api.provider.oauth.AppleAuthProvider
import com.cakk.api.provider.oauth.GoogleAuthProvider
import com.cakk.api.provider.oauth.KakaoAuthProvider
import com.cakk.common.enums.Provider
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.dispatcher.OidcProviderDispatcher

@Component
class OidcProviderDispatcherImpl(
	private val appleAuthProvider: AppleAuthProvider,
	private val kakaoAuthProvider: KakaoAuthProvider,
	private val googleAuthProvider: GoogleAuthProvider
): OidcProviderDispatcher {

    private val authProviderMap: MutableMap<Provider, OidcProvider> = EnumMap(Provider::class.java)

    init {
        initialize()
    }

    private fun initialize() {
        authProviderMap[Provider.APPLE] = appleAuthProvider
        authProviderMap[Provider.KAKAO] = kakaoAuthProvider
        authProviderMap[Provider.GOOGLE] = googleAuthProvider
    }

    override fun getProviderId(provider: Provider, idToken: String): String {
        return getProvider(provider).getProviderId(idToken)
    }

    private fun getProvider(provider: Provider): OidcProvider {
        return authProviderMap[provider] ?: throw CakkException(ReturnCode.WRONG_PROVIDER)
    }
}



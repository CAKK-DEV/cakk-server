package com.cakk.api.dispatcher

import java.util.*

import org.springframework.stereotype.Component

import com.cakk.core.provider.oauth.OidcProvider
import com.cakk.api.provider.oauth.AppleAuthProvider
import com.cakk.api.provider.oauth.GoogleAuthProvider
import com.cakk.api.provider.oauth.KakaoAuthProvider
import com.cakk.common.enums.ProviderType
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.dispatcher.OidcProviderDispatcher

@Component
class OidcProviderDispatcherImpl(
	private val appleAuthProvider: AppleAuthProvider,
	private val kakaoAuthProvider: KakaoAuthProvider,
	private val googleAuthProvider: GoogleAuthProvider
): OidcProviderDispatcher {

    private val authProviderTypeMap: MutableMap<ProviderType, OidcProvider> = EnumMap(ProviderType::class.java)

    init {
        initialize()
    }

    private fun initialize() {
        authProviderTypeMap[ProviderType.APPLE] = appleAuthProvider
        authProviderTypeMap[ProviderType.KAKAO] = kakaoAuthProvider
        authProviderTypeMap[ProviderType.GOOGLE] = googleAuthProvider
    }

    override fun getProviderId(providerType: ProviderType, idToken: String): String {
        return getProvider(providerType).getProviderId(idToken)
    }

    private fun getProvider(providerType: ProviderType): OidcProvider {
        return authProviderTypeMap[providerType] ?: throw CakkException(ReturnCode.WRONG_PROVIDER)
    }
}



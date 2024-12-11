package com.cakk.core.dispatcher

import com.cakk.common.enums.ProviderType

fun interface OidcProviderDispatcher {

	fun getProviderId(providerType: ProviderType, idToken: String): String;
}

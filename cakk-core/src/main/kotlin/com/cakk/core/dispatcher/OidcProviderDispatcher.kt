package com.cakk.core.dispatcher

import com.cakk.common.enums.Provider

fun interface OidcProviderDispatcher {

	fun getProviderId(provider: Provider, idToken: String): String;
}

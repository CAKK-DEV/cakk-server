package com.cakk.domain.generic

import com.cakk.common.enums.ProviderType
import com.cakk.domain.base.ValueObject

data class Provider(
	val providerType: ProviderType,
	val providerId: String
) : ValueObject<Provider>() {

	override fun getEqualityFields(): Array<Any> {
		return arrayOf(providerType, providerId)
	}

	companion object {

		fun of(providerType: ProviderType, providerId: String): Provider {
			return Provider(providerType, providerId)
		}
	}
}

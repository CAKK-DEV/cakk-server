package com.cakk.domain.generic

import com.cakk.domain.base.ValueObject

data class Device(
	val os: String,
	val token: String
) : ValueObject<Device>(){

	override fun getEqualityFields(): Array<Any> {
		return arrayOf(os, token)
	}

	companion object {

		fun of(os: String, token: String): Device {
			return Device(os, token)
		}
	}
}

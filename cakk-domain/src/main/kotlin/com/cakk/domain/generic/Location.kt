package com.cakk.domain.generic

import com.cakk.domain.base.ValueObject

data class Location(
	val latitude: Double,
	val longitude: Double
) : ValueObject<Location>() {

	override fun getEqualityFields(): Array<Any> {
		return arrayOf(latitude, longitude)
	}

	companion object {

		fun of(latitude: Double, longitude: Double): Location {
			return Location(latitude, longitude)
		}
	}
}

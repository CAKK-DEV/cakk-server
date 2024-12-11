package com.cakk.domain.base

abstract class ValueObject<T : ValueObject<T>> {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false

		@Suppress("UNCHECKED_CAST")
		return equalsCore(other as T)
	}

	override fun hashCode(): Int {
		var hash = 17
		getEqualityFields().forEach {
			hash = hash * 31 + (it.hashCode())
		}
		return hash
	}

	private fun equalsCore(other: T): Boolean {
		return getEqualityFields().contentEquals(other.getEqualityFields())
	}

	protected open fun getEqualityFields(): Array<Any> {
		return javaClass.declaredFields.map {
			it.isAccessible = true
			it[this]
		}.toTypedArray()
	}
}

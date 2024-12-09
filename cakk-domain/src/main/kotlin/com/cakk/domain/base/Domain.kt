package com.cakk.domain.base

abstract class Domain<T : Domain<T, TID>, TID> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || getId() == null || javaClass != other.javaClass) return false

        @Suppress("UNCHECKED_CAST")
        return getId() == (other as T).getId()
    }

    override fun hashCode(): Int {
        return getId()?.hashCode() ?: 0
    }

    abstract fun getId(): TID
}

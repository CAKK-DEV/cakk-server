package com.cakk.common.utils

fun <T> keepOnlyNElements(set: MutableSet<T>?, max: Int) {
	if (set == null || set.size <= max) {
		return
	}

	val iterator = set.iterator()
	var count = 0

	while (iterator.hasNext()) {
		iterator.next()
		count++
		if (count > max) {
			iterator.remove()
		}
	}
}

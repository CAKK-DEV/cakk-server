package com.cakk.external.extractor

fun interface MessageExtractor<T, U> {

	fun extract(message: T): U
}

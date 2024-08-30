package com.cakk.external.sender

fun interface MessageSender<T> {

	fun send( message: T)
}

package com.cakk.external.sender

fun interface MessageSender<T> {

	fun send(receiver: String, message: T)
}

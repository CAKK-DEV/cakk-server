package com.cakk.external.sender

interface MessageSender<T> {

	fun send(receiver: String, message: T)
}

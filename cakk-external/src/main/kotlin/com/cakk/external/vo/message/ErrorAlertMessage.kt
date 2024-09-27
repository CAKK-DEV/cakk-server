package com.cakk.external.vo.message

data class ErrorAlertMessage(
	val serverProfile: String,
	val stackTrace: String?,
	val contextPath: String?,
	val requestURL: String?,
	val method: String?,
	val parameterMap: Map<String, Array<String>>?,
	val remoteAddr: String?,
	val header: String?
)

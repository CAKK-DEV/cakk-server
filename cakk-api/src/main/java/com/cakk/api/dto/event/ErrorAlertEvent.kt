package com.cakk.api.dto.event

import jakarta.servlet.http.HttpServletRequest

data class ErrorAlertEvent(
	val exception: Exception,
	val request: HttpServletRequest,
	val profile: String
)

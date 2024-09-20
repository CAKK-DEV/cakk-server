package com.cakk.core.dto.event

import jakarta.servlet.http.HttpServletRequest

data class ErrorAlertEvent(
	val exception: Exception,
	val request: HttpServletRequest,
	val profile: String
)

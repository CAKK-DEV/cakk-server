package com.cakk.api.dto.event;

import jakarta.servlet.http.HttpServletRequest;

public record ErrorAlertEvent(
	Exception exception,
	HttpServletRequest request,
	String profile
) {
}

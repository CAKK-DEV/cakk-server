package com.cakk.core.mapper

import com.cakk.core.dto.event.*
import com.cakk.infrastructure.persistence.shop.CertificationEvent
import com.cakk.external.vo.message.CertificationMessage
import com.cakk.external.vo.message.ErrorAlertMessage
import com.cakk.external.vo.message.VerificationMessage
import jakarta.servlet.http.HttpServletRequest

fun supplyCakeIncreaseViewsEventBy(cakeId: Long): CakeIncreaseViewsEvent {
	return CakeIncreaseViewsEvent(cakeId)
}

fun supplyCakeShopIncreaseViewsEventBy(cakeShopId: Long): CakeShopIncreaseViewsEvent {
	return CakeShopIncreaseViewsEvent(cakeShopId)
}

fun supplyErrorAlertEventBy(
	exception: Exception,
	request: HttpServletRequest,
	profile: String
): ErrorAlertEvent {
	return ErrorAlertEvent(exception, request, profile)
}

fun supplyEmailWithVerificationCodeSendEventBy(email: String, code: String): EmailWithVerificationCodeSendEvent {
	return EmailWithVerificationCodeSendEvent(email, code)
}

fun supplyCertificationMessageBy(certificationEvent: com.cakk.infrastructure.persistence.shop.CertificationEvent): CertificationMessage {
	return CertificationMessage(
		certificationEvent.businessRegistrationImageUrl,
		certificationEvent.idCardImageUrl,
		certificationEvent.emergencyContact,
		certificationEvent.message,
		certificationEvent.userId,
		certificationEvent.userEmail,
		certificationEvent.shopName,
		certificationEvent.location.y,
		certificationEvent.location.x
	)
}

fun supplyVerificationMessageBy(event: EmailWithVerificationCodeSendEvent): VerificationMessage {
	return VerificationMessage(event.email, event.code)
}

fun supplyErrorAlertMessageBy(event: ErrorAlertEvent): ErrorAlertMessage {
	val profile: String = event.profile
	val stackTrace: String = event.exception.stackTrace.contentToString()
	val request: HttpServletRequest = event.request

	return ErrorAlertMessage(
		profile,
		stackTrace,
		request.contextPath,
		request.requestURL.toString(),
		request.method,
		request.parameterMap,
		request.remoteAddr,
		request.getHeader("User-Agent")
	)
}

fun supplyIncreaseSearchCountEventBy(keyword: String): IncreaseSearchCountEvent {
	return IncreaseSearchCountEvent(keyword)
}

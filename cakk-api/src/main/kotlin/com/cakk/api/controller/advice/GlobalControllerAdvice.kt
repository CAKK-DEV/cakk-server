package com.cakk.api.controller.advice

import java.sql.SQLException

import jakarta.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.common.response.ApiResponse
import com.cakk.core.mapper.supplyErrorAlertEventBy

@RestControllerAdvice
class GlobalControllerAdvice(
	private val applicationEventPublisher: ApplicationEventPublisher,
	@Value("\${spring.profiles.active}")
	private val profile: String
) {

	@ExceptionHandler(CakkException::class)
	fun handleCakkException(exception: CakkException, request: HttpServletRequest): ResponseEntity<ApiResponse<Unit>> {
		val returnCode = exception.getReturnCode()

		if (returnCode == ReturnCode.INTERNAL_SERVER_ERROR || returnCode == ReturnCode.EXTERNAL_SERVER_ERROR) {
			applicationEventPublisher.publishEvent(supplyErrorAlertEventBy(exception, request, profile))
		}

		return getResponseEntity(HttpStatus.BAD_REQUEST, ApiResponse.fail(exception.getReturnCode()))
	}

	@ExceptionHandler(
		value = [
			HttpMessageNotReadableException::class,
			MissingServletRequestParameterException::class,
			MethodArgumentTypeMismatchException::class
		]
	)
	fun handleRequestException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
		return getResponseEntity(HttpStatus.BAD_REQUEST, ApiResponse.fail(ReturnCode.WRONG_PARAMETER))
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException::class)
	fun handleMethodNotSupportedException(exception: HttpRequestMethodNotSupportedException): ResponseEntity<ApiResponse<Unit>> {
		return getResponseEntity(HttpStatus.BAD_REQUEST, ApiResponse.fail(ReturnCode.METHOD_NOT_ALLOWED))
	}

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleMethodArgNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Map<String, String>>> {
		val errors: MutableMap<String, String> = HashMap()
		exception.bindingResult.allErrors.forEach {
			val fieldName = (it as FieldError).field
			val errorMessage = it.getDefaultMessage()
			errorMessage?.let { errors[fieldName] = errorMessage }
		}

		return getResponseEntity(HttpStatus.BAD_REQUEST, ApiResponse.fail(ReturnCode.WRONG_PARAMETER, errors))
	}

	@ExceptionHandler(
		value = [
			SQLException::class,
			RuntimeException::class
		]
	)
	fun handleServerException(exception: Exception, request: HttpServletRequest): ResponseEntity<ApiResponse<String>> {
		applicationEventPublisher.publishEvent(supplyErrorAlertEventBy(exception, request, profile))

		return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ApiResponse.error(ReturnCode.INTERNAL_SERVER_ERROR, exception.message))
	}

	private fun <T> getResponseEntity(status: HttpStatus, response: ApiResponse<T>): ResponseEntity<ApiResponse<T>> {
		return ResponseEntity.status(status).body(response)
	}
}

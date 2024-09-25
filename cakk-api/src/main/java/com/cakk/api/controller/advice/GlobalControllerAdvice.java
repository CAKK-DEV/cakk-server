package com.cakk.api.controller.advice;

import static com.cakk.core.mapper.EventMapperKt.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.common.response.ApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

	private final ApplicationEventPublisher applicationEventPublisher;

	private final String profile;

	public GlobalControllerAdvice(
		ApplicationEventPublisher applicationEventPublisher,
		@Value("${spring.profiles.active}") String profile
	) {
		this.applicationEventPublisher = applicationEventPublisher;
		this.profile = profile;
	}

	@ExceptionHandler(CakkException.class)
	public ResponseEntity<ApiResponse<Void>> handleCakkException(CakkException exception, HttpServletRequest request) {
		final ReturnCode returnCode = exception.getReturnCode();

		if (returnCode.equals(ReturnCode.INTERNAL_SERVER_ERROR) || returnCode.equals(ReturnCode.EXTERNAL_SERVER_ERROR)) {
			applicationEventPublisher.publishEvent(supplyErrorAlertEventBy(exception, request, profile));
		}

		log.error(exception.getMessage());
		return getResponseEntity(BAD_REQUEST, ApiResponse.fail(exception.getReturnCode()));
	}

	@ExceptionHandler(value = {
		HttpMessageNotReadableException.class,
		MissingServletRequestParameterException.class,
		MethodArgumentTypeMismatchException.class
	})
	public ResponseEntity<ApiResponse<Void>> handleRequestException(Exception exception) {
		log.error(exception.getMessage());
		return getResponseEntity(BAD_REQUEST, ApiResponse.fail(ReturnCode.WRONG_PARAMETER));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		log.error(exception.getMessage());
		return getResponseEntity(BAD_REQUEST, ApiResponse.fail(ReturnCode.METHOD_NOT_ALLOWED));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgNotValidException(MethodArgumentNotValidException exception) {
		Map<String, String> errors = new HashMap<>();
		exception.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError)error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		log.error("Validation failed: {}", errors);
		return getResponseEntity(BAD_REQUEST, ApiResponse.fail(ReturnCode.WRONG_PARAMETER, errors));
	}

	@ExceptionHandler(value = {
		SQLException.class,
		RuntimeException.class
	})
	public ResponseEntity<ApiResponse<String>> handleServerException(Exception exception, HttpServletRequest request) {
		applicationEventPublisher.publishEvent(supplyErrorAlertEventBy(exception, request, profile));
		log.error(exception.getMessage());

		return getResponseEntity(INTERNAL_SERVER_ERROR, ApiResponse.error(ReturnCode.INTERNAL_SERVER_ERROR, exception.getMessage()));
	}

	private <T> ResponseEntity<ApiResponse<T>> getResponseEntity(HttpStatus status, ApiResponse<T> response) {
		return ResponseEntity.status(status).body(response);
	}
}

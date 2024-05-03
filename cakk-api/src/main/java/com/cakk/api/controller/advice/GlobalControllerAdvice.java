package com.cakk.api.controller.advice;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.RequiredArgsConstructor;

import com.cakk.api.service.slack.SlackService;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.common.response.ApiResponse;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

	private final SlackService slackService;

	@ExceptionHandler(CakkException.class)
	public ResponseEntity<ApiResponse<Void>> handleCakkException(CakkException exception, HttpServletRequest request) {
		if (exception.getReturnCode().equals(ReturnCode.EXTERNAL_SERVER_ERROR)) {
			slackService.sendSlackForError(exception, request);
		}

		return getResponseEntity(BAD_REQUEST, ApiResponse.fail(exception.getReturnCode()));
	}

	@ExceptionHandler(value = {
		HttpMessageNotReadableException.class,
		MissingServletRequestParameterException.class,
		MethodArgumentTypeMismatchException.class
	})
	public ResponseEntity<ApiResponse<Void>> handleRequestException(Exception exception) {
		return getResponseEntity(BAD_REQUEST, ApiResponse.fail(ReturnCode.WRONG_PARAMETER));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		return getResponseEntity(BAD_REQUEST, ApiResponse.fail(ReturnCode.METHOD_NOT_ALLOWED));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<List<FieldError>>> badRequestExHandler(BindingResult bindingResult) {
		return getResponseEntity(BAD_REQUEST, ApiResponse.fail(ReturnCode.WRONG_PARAMETER, bindingResult.getFieldErrors()));
	}

	@ExceptionHandler(value = {
		SQLException.class,
		RuntimeException.class
	})
	public ResponseEntity<ApiResponse<String>> handleServerException(SQLException exception, HttpServletRequest request) {
		slackService.sendSlackForError(exception, request);
		return getResponseEntity(INTERNAL_SERVER_ERROR, ApiResponse.error(ReturnCode.INTERNAL_SERVER_ERROR, exception.getMessage()));
	}

	private <T> ResponseEntity<ApiResponse<T>> getResponseEntity(HttpStatus status, ApiResponse<T> response) {
		return ResponseEntity.status(status).body(response);
	}
}

package com.cakk.common.response;

import lombok.Getter;

import com.cakk.common.enums.ReturnCode;

@Getter
public class ApiResponse<T> {

	private String returnCode;
	private String returnMessage;
	private T data;

	public static <T> ApiResponse<T> success(T data) {
		ApiResponse<T> response = new ApiResponse<>();

		response.returnCode = ReturnCode.SUCCESS.getCode();
		response.returnMessage = ReturnCode.SUCCESS.getMessage();
		response.data = data;

		return response;
	}

	public static <T> ApiResponse<T> fail(ReturnCode returnCode, T data) {
		ApiResponse<T> response = new ApiResponse<>();

		response.returnCode = returnCode.getCode();
		response.returnMessage = returnCode.getMessage();
		response.data = data;

		return response;
	}

	public static ApiResponse<String> error(ReturnCode returnCode, String errorMessage) {
		ApiResponse<String> response = new ApiResponse<>();

		response.returnCode = returnCode.getCode();
		response.returnMessage = returnCode.getMessage();
		response.data = errorMessage;

		return response;
	}
}

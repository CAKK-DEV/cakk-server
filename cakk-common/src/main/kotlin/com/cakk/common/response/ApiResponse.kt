package com.cakk.common.response

import com.cakk.common.enums.ReturnCode

data class ApiResponse<T>(
	val returnCode: String,
	val returnMessage: String,
	val data: T? = null
) {

	constructor(returnCode: ReturnCode, data: T? = null) : this(returnCode.code, returnCode.message, data)

	companion object {

		fun <T> success(data: T? = null): ApiResponse<T> {
			return ApiResponse(
				returnCode = ReturnCode.SUCCESS,
				data = data
			)
		}

		fun <T> fail(returnCode: ReturnCode, data: T? = null): ApiResponse<T> {
			return ApiResponse(
				returnCode = returnCode,
				data = data
			)
		}

		fun error(returnCode: ReturnCode, errorMessage: String?): ApiResponse<String> {
			return ApiResponse(
				returnCode = returnCode,
				data = errorMessage
			)
		}
	}
}

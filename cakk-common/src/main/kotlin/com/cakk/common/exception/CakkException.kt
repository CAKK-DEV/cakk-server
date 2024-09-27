package com.cakk.common.exception

import com.cakk.common.enums.ReturnCode

class CakkException(
	private val returnCode: ReturnCode
) : RuntimeException() {

    private val code
		get() = returnCode.code
    override val message: String
		get() = returnCode.message

	fun getReturnCode(): ReturnCode {
		return returnCode
	}
}

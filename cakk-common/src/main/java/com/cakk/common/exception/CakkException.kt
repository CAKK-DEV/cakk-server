package com.cakk.common.exception;

import lombok.Getter;

import com.cakk.common.enums.ReturnCode;

@Getter
public class CakkException extends RuntimeException {

	private final ReturnCode returnCode;
	private final String code;
	private final String message;

	public CakkException(ReturnCode returnCode) {
		this.returnCode = returnCode;
		this.code = returnCode.getCode();
		this.message = returnCode.getMessage();
	}
}

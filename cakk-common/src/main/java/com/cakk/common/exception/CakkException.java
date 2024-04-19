package com.cakk.common.exception;

import com.cakk.common.enums.ReturnCode;
import lombok.Getter;

@Getter
public class CakkException extends RuntimeException {

	private final String code;
	private final String message;

public CakkException(ReturnCode returnCode) {
		this.code = returnCode.getCode();
		this.message = returnCode.getMessage();
	}
}

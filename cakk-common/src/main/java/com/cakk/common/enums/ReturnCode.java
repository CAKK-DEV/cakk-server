package com.cakk.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReturnCode {

	SUCCESS("1000", "요청에 성공하셨습니다.");

	private final String code;
	private final String message;
}

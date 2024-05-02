package com.cakk.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReturnCode {

	SUCCESS("1000", "요청에 성공하셨습니다."),

	// 서버 에러 (9998, 9999)
	INTERNAL_SERVER_ERROR("9998", "내부 서버 에러 입니다."),
	EXTERNAL_SERVER_ERROR("9999", "외부 서버 에러 입니다.");

	private final String code;
	private final String message;
}

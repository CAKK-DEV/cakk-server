package com.cakk.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKey {

	SEARCH_KEYWORD("SEARCH::keyword");

	private final String value;
}

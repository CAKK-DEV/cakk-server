package com.cakk.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKey {

	SEARCH_KEYWORD("SEARCH::keyword"),
	LOCK_CAKE_LIKE("LOCK::cake-like"),
	LOCK_SHOP_LIKE("LOCK::shop-like");

	private final String value;
}

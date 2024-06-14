package com.cakk.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKey {

	SEARCH_KEYWORD("SEARCH::keyword"),
	VIEWS_CAKE("VIEWS::cake"),
	LOCK_CAKE_HEART("LOCK::cake-heart"),
	LOCK_SHOP_HEART("LOCK::shop-heart");

	private final String value;
}

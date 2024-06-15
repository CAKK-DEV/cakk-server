package com.cakk.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.cakk.common.exception.CakkException;

@Getter
@RequiredArgsConstructor
public enum RedisKey {

	SEARCH_KEYWORD("SEARCH::keyword"),
	VIEWS_CAKE("VIEWS::cake"),
	LOCK_CAKE_HEART("LOCK::cake-heart"),
	LOCK_SHOP_HEART("LOCK::shop-heart"),
	LOCK_SHOP_LIKE("LOCK::shop-like");

	private final String value;

	public static RedisKey getLockByMethodName(String method) {
		return switch (method) {
			case "heartCake" -> LOCK_CAKE_HEART;
			case "heartCakeShop" -> LOCK_SHOP_HEART;
			case "likeCakeShop" -> LOCK_SHOP_LIKE;
			default -> throw new CakkException(ReturnCode.INTERNAL_SERVER_ERROR);
		};
	}
}

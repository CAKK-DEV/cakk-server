package com.cakk.infrastructure.persistence.param.like;

public record HeartCakeImageResponseParam(
	Long cakeShopId,
	Long cakeId,
	Long cakeHeartId,
	String cakeImageUrl
) {
}

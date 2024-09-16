package com.cakk.domain.mysql.dto.param.like;

public record HeartCakeImageResponseParam(
	Long cakeShopId,
	Long cakeId,
	Long cakeHeartId,
	String cakeImageUrl
) {
}

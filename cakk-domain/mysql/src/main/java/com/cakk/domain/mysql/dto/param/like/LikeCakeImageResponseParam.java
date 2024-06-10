package com.cakk.domain.mysql.dto.param.like;

public record LikeCakeImageResponseParam(
	Long cakeShopId,
	Long cakeId,
	Long cakeLikeId,
	String cakeImageUrl
) {
}

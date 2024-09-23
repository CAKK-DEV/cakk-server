package com.cakk.api.dto.response.like;

import java.util.List;

import lombok.Builder;

import com.cakk.domain.mysql.dto.param.like.HeartCakeShopResponseParam;

@Builder
public record HeartCakeShopListResponse(

	List<HeartCakeShopResponseParam> cakeShops,
	Long lastCakeShopHeartId,
	int size
) {
}

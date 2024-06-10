package com.cakk.api.dto.response.like;

import java.util.List;

import lombok.Builder;

import com.cakk.domain.mysql.dto.param.like.LikeCakeImageResponseParam;

@Builder
public record LikeCakeImageListResponse(

	List<LikeCakeImageResponseParam> cakeImages,
	Long lastCakeLikeId,
	int size
) {
}

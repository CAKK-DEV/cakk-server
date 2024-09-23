package com.cakk.api.dto.response.like;

import java.util.List;

import lombok.Builder;

import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;

@Builder
public record HeartCakeImageListResponse(

	List<HeartCakeImageResponseParam> cakeImages,
	Long lastCakeHeartId,
	int size
) {
}

package com.cakk.api.dto.response.cake;

import java.util.List;

import lombok.Builder;

import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;

@Builder
public record CakeImageListResponse(

	List<CakeImageResponseParam> cakeImages,
	Long lastCakeId,
	int size
) {
}

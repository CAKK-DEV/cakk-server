package com.cakk.api.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.response.like.HeartResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeartMapper {

	public static HeartResponse supplyHeartResponseBy(final boolean isHeart) {
		return new HeartResponse(isHeart);
	}
}

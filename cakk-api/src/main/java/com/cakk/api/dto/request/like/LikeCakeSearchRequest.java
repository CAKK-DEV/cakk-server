package com.cakk.api.dto.request.like;

import jakarta.validation.constraints.NotNull;

public record LikeCakeSearchRequest(
	Long cakeLikeId,
	@NotNull
	Integer pageSize
) {
}

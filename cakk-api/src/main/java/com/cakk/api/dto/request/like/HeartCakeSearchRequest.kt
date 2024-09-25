package com.cakk.api.dto.request.like;

import jakarta.validation.constraints.NotNull;

public record HeartCakeSearchRequest(
	Long cakeHeartId,
	@NotNull
	Integer pageSize
) {
}

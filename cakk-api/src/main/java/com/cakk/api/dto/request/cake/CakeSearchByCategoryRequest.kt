package com.cakk.api.dto.request.cake;

import jakarta.validation.constraints.NotNull;

import com.cakk.common.enums.CakeDesignCategory;

public record CakeSearchByCategoryRequest(
	Long cakeId,
	@NotNull
	CakeDesignCategory category,
	@NotNull
	Integer pageSize
) {
}

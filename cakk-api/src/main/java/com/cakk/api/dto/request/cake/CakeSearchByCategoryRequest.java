package com.cakk.api.dto.request.cake;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.cakk.common.enums.CakeDesignCategory;

public record CakeSearchByCategoryRequest(
	Long cakeId,
	@NotNull
	CakeDesignCategory category,
	@NotNull
	Integer pageSize
) {
}

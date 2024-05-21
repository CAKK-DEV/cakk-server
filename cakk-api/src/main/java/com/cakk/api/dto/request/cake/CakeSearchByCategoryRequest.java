package com.cakk.api.dto.request.cake;

import com.cakk.common.enums.CakeDesignCategory;

public record CakeSearchByCategoryRequest(

	Long cakeId,
	CakeDesignCategory category,
	int pageSize
) {
}

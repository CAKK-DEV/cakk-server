package com.cakk.api.dto.request.cake;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.cakk.api.mapper.CakeDesignCategoryMapper;
import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.mysql.dto.param.cake.CakeUpdateParam;
import com.cakk.domain.mysql.entity.user.User;

public record CakeUpdateRequest(
	@NotBlank
	String cakeImageUrl,
	@NotNull
	List<CakeDesignCategory> cakeDesignCategories,
	@NotNull
	List<String> tagNames
) {

	public CakeUpdateParam toParam(User owner, Long cakeId) {
		return new CakeUpdateParam(
			owner,
			cakeId,
			cakeImageUrl,
			CakeDesignCategoryMapper.supplyCakeCategoryListBy(cakeDesignCategories),
			tagNames
		);
	}
}

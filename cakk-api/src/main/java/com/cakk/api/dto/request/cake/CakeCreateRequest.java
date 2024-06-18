package com.cakk.api.dto.request.cake;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import com.cakk.api.mapper.CakeDesignCategoryMapper;
import com.cakk.api.mapper.CakeMapper;
import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.mysql.dto.param.cake.CakeCreateParam;
import com.cakk.domain.mysql.entity.user.User;

public record CakeCreateRequest(
	@NotBlank
	String cakeImageUrl,
	List<CakeDesignCategory> cakeDesignCategories,
	List<String> tagNames
) {

	public CakeCreateParam toParam(User user, Long cakeShopId) {
		return new CakeCreateParam(
			CakeMapper.supplyCakeBy(cakeImageUrl),
			CakeDesignCategoryMapper.supplyCakeCategoryListBy(cakeDesignCategories),
			tagNames,
			user,
			cakeShopId
		);
	}
}

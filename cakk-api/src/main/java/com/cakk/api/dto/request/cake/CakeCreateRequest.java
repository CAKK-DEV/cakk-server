package com.cakk.api.dto.request.cake;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.cakk.api.mapper.CakeDesignCategoryMapper;
import com.cakk.api.mapper.CakeMapper;
import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.mysql.dto.param.cake.CakeCreateParam;
import com.cakk.domain.mysql.entity.user.User;

public record CakeCreateRequest(
	@NotBlank @Size(max = 200)
	String cakeImageUrl,
	@NotNull
	List<CakeDesignCategory> cakeDesignCategories,
	@NotNull
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

package com.cakk.api.mapper;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.mysql.entity.cake.CakeCategory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeDesignCategoryMapper {

	public static List<CakeCategory> supplyCakeCategoryListBy(final List<CakeDesignCategory> cakeDesignCategories) {
		return cakeDesignCategories
			.stream()
			.map(CakeDesignCategoryMapper::supplyCakeCategoryBy)
			.toList();
	}

	public static CakeCategory supplyCakeCategoryBy(CakeDesignCategory cakeDesignCategory) {
		return CakeCategory.builder()
			.cakeDesignCategory(cakeDesignCategory)
			.build();
	}
}

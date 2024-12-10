package com.cakk.core.mapper

import com.cakk.common.enums.CakeDesignCategory
import com.cakk.infrastructure.persistence.entity.cake.CakeCategoryEntity

fun supplyCakeCategoryListBy(cakeDesignCategories: List<CakeDesignCategory>): List<CakeCategoryEntity> {
	return cakeDesignCategories.map { supplyCakeCategoryBy(it) }.toList()
}

private fun supplyCakeCategoryBy(cakeDesignCategory: CakeDesignCategory): CakeCategoryEntity {
	return CakeCategoryEntity.builder()
		.cakeDesignCategory(cakeDesignCategory)
		.build()
}

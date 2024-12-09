package com.cakk.admin.mapper

import com.cakk.common.enums.CakeDesignCategory
import com.cakk.infrastructure.persistence.entity.cake.CakeCategory

fun supplyCakeCategoryListBy(cakeDesignCategories: List<CakeDesignCategory>): List<com.cakk.infrastructure.persistence.entity.cake.CakeCategory> {
	return cakeDesignCategories.map { supplyCakeCategoryBy(it) }.toList()
}

private fun supplyCakeCategoryBy(cakeDesignCategory: CakeDesignCategory?): com.cakk.infrastructure.persistence.entity.cake.CakeCategory {
	return com.cakk.infrastructure.persistence.entity.cake.CakeCategory.builder()
		.cakeDesignCategory(cakeDesignCategory)
		.build()
}

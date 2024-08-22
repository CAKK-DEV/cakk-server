package com.cakk.admin.mapper

import com.cakk.common.enums.CakeDesignCategory
import com.cakk.domain.mysql.entity.cake.CakeCategory

fun supplyCakeCategoryListBy(cakeDesignCategories: List<CakeDesignCategory>): List<CakeCategory> {
    return cakeDesignCategories.map { supplyCakeCategoryBy(it) }.toList()
}

fun supplyCakeCategoryBy(cakeDesignCategory: CakeDesignCategory?): CakeCategory {
    return CakeCategory.builder()
        .cakeDesignCategory(cakeDesignCategory)
        .build()
}

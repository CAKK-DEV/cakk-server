package com.cakk.core.dto.response.cake

import com.cakk.common.enums.CakeDesignCategory
import com.cakk.domain.mysql.dto.param.tag.TagParam

data class CakeDetailResponse(
    val cakeImageUrl: String,
    val cakeShopName: String,
    val shopBio: String,
    val cakeShopId: Long,
    val cakeCategories: Set<CakeDesignCategory>,
    val tags: Set<TagParam>
)

package com.cakk.admin.mapper

import com.cakk.admin.dto.response.CakeDetailResponse
import com.cakk.admin.dto.response.TagResponse
import com.cakk.domain.mysql.dto.param.cake.CakeDetailParam
import com.cakk.domain.mysql.entity.cake.Cake

fun supplyCakeBy(cakeImageUrl: String): Cake {
    return Cake.builder()
        .cakeImageUrl(cakeImageUrl)
        .build()
}

fun supplyCakeDetailResponseBy(param: CakeDetailParam): CakeDetailResponse {
    val tags = param.tags?.let { tags ->
        tags.map { TagResponse(it.tagId, it.tagName) }
    }?.toMutableSet() ?: mutableSetOf()

    return CakeDetailResponse(
        cakeShopId = param.cakeShopId,
        cakeImageUrl = param.cakeImageUrl,
        cakeShopName = param.cakeShopName,
        shopBio = param.shopBio,
        cakeCategories = param.cakeCategories,
        tags = tags
    )
}

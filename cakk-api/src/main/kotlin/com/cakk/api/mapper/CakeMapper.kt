package com.cakk.api.mapper

import com.cakk.api.dto.request.cake.*
import com.cakk.core.dto.param.cake.*
import com.cakk.core.mapper.supplyCakeBy
import com.cakk.core.mapper.supplyCakeCategoryListBy
import com.cakk.core.mapper.supplyPointBy
import com.cakk.infrastructure.persistence.entity.user.UserEntity

fun supplyCakeSearchByCategoryParamBy(dto: CakeSearchByCategoryRequest): CakeSearchByCategoryParam {
    return CakeSearchByCategoryParam(
        dto.cakeId,
        dto.category!!,
        dto.pageSize
    )
}

fun supplyCakeSearchParamBy(dto: CakeSearchByLocationRequest): CakeSearchParam {
    return CakeSearchParam(
        dto.cakeId,
        dto.keyword,
        supplyPointBy(dto.latitude, dto.longitude),
        dto.pageSize!!
    )
}

fun supplyCakeSearchByShopParamBy(dto: CakeSearchByShopRequest): CakeSearchByShopParam {
    return CakeSearchByShopParam(
        dto.cakeId,
        dto.cakeShopId!!,
        dto.pageSize!!
    )
}

fun supplyCakeSearchByViewsParamBy(dto: CakeSearchByViewsRequest): CakeSearchByViewsParam {
    return CakeSearchByViewsParam(
        dto.offset,
        dto.pageSize!!
    )
}

fun supplyCakeCreateParamBy(dto: CakeCreateRequest, userEntity: UserEntity, cakeShopId: Long): CakeCreateParam {
    return CakeCreateParam(
        supplyCakeBy(dto.cakeImageUrl!!),
        supplyCakeCategoryListBy(dto.cakeDesignCategories!!),
        dto.tagNames!!,
        userEntity,
        cakeShopId
    )
}

fun supplyCakeUpdateParamBy(dto: CakeUpdateRequest, owner: UserEntity, cakeId: Long): CakeUpdateParam {
    return CakeUpdateParam(
        owner = owner,
        cakeId = cakeId,
        cakeImageUrl = dto.cakeImageUrl!!,
        cakeCategories = supplyCakeCategoryListBy(dto.cakeDesignCategories!!),
        tagNames = dto.tagNames!!
    )
}

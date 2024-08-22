package com.cakk.admin.dto.request

import com.cakk.admin.dto.param.CakeCreateByAdminParam
import com.cakk.admin.mapper.supplyCakeBy
import com.cakk.admin.mapper.supplyCakeCategoryListBy
import com.cakk.common.enums.CakeDesignCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CakeCreateByAdminRequest(
    @field:NotBlank @field:Size(max = 100)
    val cakeImageUrl: String,
    @field:NotNull
    val cakeDesignCategories: List<CakeDesignCategory>,
    @field:NotNull
    val tagNames: List<String>
) {

    fun toParam(cakeShopId: Long): CakeCreateByAdminParam {
        return CakeCreateByAdminParam(
            cake = supplyCakeBy(cakeImageUrl),
            cakeCategories = supplyCakeCategoryListBy(cakeDesignCategories),
            tagNames = tagNames,
            cakeShopId = cakeShopId
        )
    }
}

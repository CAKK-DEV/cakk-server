package com.cakk.admin.dto.request

import com.cakk.admin.dto.param.CakeUpdateByAdminParam
import com.cakk.admin.mapper.supplyCakeCategoryListBy
import com.cakk.common.enums.CakeDesignCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CakeUpdateByAdminRequest(
    @field:NotBlank @field:Size(max = 200)
    val cakeImageUrl: String,
    @field:NotNull
    val cakeDesignCategories: List<CakeDesignCategory>,
    @field:NotNull
    val tagNames: List<String>
) {

    fun toParam(cakeId: Long): CakeUpdateByAdminParam {
        return CakeUpdateByAdminParam(
            cakeId = cakeId,
            cakeImageUrl = cakeImageUrl,
            cakeCategories = supplyCakeCategoryListBy(cakeDesignCategories),
            tagNames = tagNames
        )
    }
}

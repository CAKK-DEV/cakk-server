package com.cakk.admin.dto.request

import com.cakk.domain.mysql.dto.param.shop.CakeShopUpdateParam
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CakeShopUpdateByAdminRequest(
    @field:Size(max = 200)
    val thumbnailUrl: String?,
    @field:NotBlank @Size(max = 30)
    val shopName: String,
    @field:Size(max = 40)
    val shopBio: String?,
    @field:Size(max = 500)
    val shopDescription: String?
) {

    fun toParam(cakeShopId: Long): CakeShopUpdateParam {
        return CakeShopUpdateParam.builder()
            .cakeShopId(cakeShopId)
            .thumbnailUrl(thumbnailUrl)
            .shopName(shopName)
            .shopBio(shopBio)
            .shopDescription(shopDescription)
            .build()
    }
}

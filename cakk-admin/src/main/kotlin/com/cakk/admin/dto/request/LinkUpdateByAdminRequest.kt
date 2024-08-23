package com.cakk.admin.dto.request

import com.cakk.admin.mapper.supplyCakeShopLinkByInstagram
import com.cakk.admin.mapper.supplyCakeShopLinkByKakao
import com.cakk.admin.mapper.supplyCakeShopLinkByWeb
import com.cakk.domain.mysql.dto.param.link.UpdateLinkParam
import com.cakk.domain.mysql.entity.shop.CakeShopLink
import jakarta.validation.constraints.Size

data class LinkUpdateByAdminRequest(
    @field:Size(min = 1, max = 200)
    val instagram: String?,
    @field:Size(min = 1, max = 200)
    val kakao: String?,
    @field:Size(min = 1, max = 200)
    val web: String?
) {

    fun toParam(cakeShopId: Long): UpdateLinkParam {
        val cakeShopLinks = mutableListOf<CakeShopLink>()

        instagram?.let { cakeShopLinks.add(supplyCakeShopLinkByInstagram(it)) }
        kakao?.let { cakeShopLinks.add(supplyCakeShopLinkByKakao(it)) }
        web?.let { cakeShopLinks.add(supplyCakeShopLinkByWeb(it)) }

        return UpdateLinkParam.builder()
            .cakeShopId(cakeShopId)
            .cakeShopLinks(cakeShopLinks)
            .build()
    }
}

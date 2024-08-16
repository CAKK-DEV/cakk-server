package com.cakk.admin.mapper

import com.cakk.admin.dto.param.ShopLinkParam
import com.cakk.common.enums.LinkKind
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.shop.CakeShopLink

fun supplyCakeShopLinksBy(cakeShop: CakeShop, links: List<ShopLinkParam>): List<CakeShopLink> {
    return links.map {
        when (it.linkKind) {
            LinkKind.WEB -> supplyCakeShopLinkByWeb(cakeShop, it.linkPath)
            LinkKind.INSTAGRAM -> supplyCakeShopLinkByInstagram(cakeShop, it.linkPath)
            LinkKind.KAKAOTALK -> supplyCakeShopLinkByKakao(cakeShop, it.linkPath)
        }
    }.toList()
}

private fun supplyCakeShopLinkByWeb(cakeShop: CakeShop, web: String): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.WEB)
        .linkPath(web)
        .cakeShop(cakeShop)
        .build()
}

private fun supplyCakeShopLinkByInstagram(cakeShop: CakeShop, instagram: String): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.INSTAGRAM)
        .linkPath(instagram)
        .cakeShop(cakeShop)
        .build()
}

private fun supplyCakeShopLinkByKakao(cakeShop: CakeShop, kakao: String): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.KAKAOTALK)
        .linkPath(kakao)
        .cakeShop(cakeShop)
        .build()
}

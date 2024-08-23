package com.cakk.admin.mapper

import com.cakk.admin.dto.param.ShopLinkParam
import com.cakk.common.enums.LinkKind
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.shop.CakeShopLink

fun supplyCakeShopLinksBy(cakeShop: CakeShop, links: List<ShopLinkParam>): List<CakeShopLink> {
    return links.map {
        when (it.linkKind) {
            LinkKind.WEB -> supplyCakeShopLinkByWeb(it.linkPath, cakeShop)
            LinkKind.INSTAGRAM -> supplyCakeShopLinkByInstagram(it.linkPath, cakeShop)
            LinkKind.KAKAOTALK -> supplyCakeShopLinkByKakao(it.linkPath, cakeShop)
        }
    }.toList()
}

fun supplyCakeShopLinkByWeb(web: String, cakeShop: CakeShop? = null): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.WEB)
        .linkPath(web)
        .cakeShop(cakeShop)
        .build()
}

fun supplyCakeShopLinkByInstagram(instagram: String, cakeShop: CakeShop? = null): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.INSTAGRAM)
        .linkPath(instagram)
        .cakeShop(cakeShop)
        .build()
}

fun supplyCakeShopLinkByKakao(kakao: String, cakeShop: CakeShop? = null): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.KAKAOTALK)
        .linkPath(kakao)
        .cakeShop(cakeShop)
        .build()
}

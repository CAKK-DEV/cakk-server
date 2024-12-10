package com.cakk.admin.mapper

import com.cakk.common.enums.LinkKind
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLinkEntity

fun supplyCakeShopLinkByWeb(web: String, cakeShop: CakeShopEntity? = null): CakeShopLinkEntity {
	return CakeShopLinkEntity.builder()
		.linkKind(LinkKind.WEB)
		.linkPath(web)
		.cakeShop(cakeShop)
		.build()
}

fun supplyCakeShopLinkByInstagram(instagram: String, cakeShop: CakeShopEntity? = null): CakeShopLinkEntity {
	return CakeShopLinkEntity.builder()
		.linkKind(LinkKind.INSTAGRAM)
		.linkPath(instagram)
		.cakeShop(cakeShop)
		.build()
}

fun supplyCakeShopLinkByKakao(kakao: String, cakeShop: CakeShopEntity? = null): CakeShopLinkEntity {
	return CakeShopLinkEntity.builder()
		.linkKind(LinkKind.KAKAOTALK)
		.linkPath(kakao)
		.cakeShop(cakeShop)
		.build()
}

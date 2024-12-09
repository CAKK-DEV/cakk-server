package com.cakk.admin.mapper

import com.cakk.common.enums.LinkKind
import com.cakk.infrastructure.persistence.entity.shop.CakeShop
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLink

fun supplyCakeShopLinkByWeb(web: String, cakeShop: com.cakk.infrastructure.persistence.entity.shop.CakeShop? = null): com.cakk.infrastructure.persistence.entity.shop.CakeShopLink {
	return com.cakk.infrastructure.persistence.entity.shop.CakeShopLink.builder()
		.linkKind(LinkKind.WEB)
		.linkPath(web)
		.cakeShop(cakeShop)
		.build()
}

fun supplyCakeShopLinkByInstagram(instagram: String, cakeShop: com.cakk.infrastructure.persistence.entity.shop.CakeShop? = null): com.cakk.infrastructure.persistence.entity.shop.CakeShopLink {
	return com.cakk.infrastructure.persistence.entity.shop.CakeShopLink.builder()
		.linkKind(LinkKind.INSTAGRAM)
		.linkPath(instagram)
		.cakeShop(cakeShop)
		.build()
}

fun supplyCakeShopLinkByKakao(kakao: String, cakeShop: com.cakk.infrastructure.persistence.entity.shop.CakeShop? = null): com.cakk.infrastructure.persistence.entity.shop.CakeShopLink {
	return com.cakk.infrastructure.persistence.entity.shop.CakeShopLink.builder()
		.linkKind(LinkKind.KAKAOTALK)
		.linkPath(kakao)
		.cakeShop(cakeShop)
		.build()
}

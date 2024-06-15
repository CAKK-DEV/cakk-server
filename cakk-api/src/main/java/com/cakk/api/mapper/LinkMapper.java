package com.cakk.api.mapper;

import com.cakk.common.enums.LinkKind;
import com.cakk.domain.mysql.entity.shop.CakeShopLink;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LinkMapper {

	public static CakeShopLink supplyCakeShopLinkByInstagram(String instagram) {
		if (instagram == null) {
			return null;
		}
		return CakeShopLink.builder()
			.linkKind(LinkKind.INSTAGRAM)
			.linkPath(instagram)
			.build();
	}

	public static CakeShopLink supplyCakeShopLinkByKakao(String kakao) {
		if (kakao == null) {
			return null;
		}
		return CakeShopLink.builder()
			.linkKind(LinkKind.KAKAOTALK)
			.linkPath(kakao)
			.build();
	}

	public static CakeShopLink supplyCakeShopLinkByWeb(String web) {
		if (web == null) {
			return null;
		}
		return CakeShopLink.builder()
			.linkKind(LinkKind.WEB)
			.linkPath(web)
			.build();
	}
}

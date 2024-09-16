package com.cakk.domain.mysql.dto.param.shop;

import com.cakk.common.enums.LinkKind;

public record CakeShopLinkParam(
	LinkKind linkKind,
	String linkPath
) {
}

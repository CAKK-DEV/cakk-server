package com.cakk.infrastructure.persistence.param.shop;

import com.cakk.common.enums.LinkKind;

public record CakeShopLinkParam(
	LinkKind linkKind,
	String linkPath
) {
}

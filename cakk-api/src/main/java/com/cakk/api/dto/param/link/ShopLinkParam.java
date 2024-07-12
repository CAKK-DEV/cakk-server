package com.cakk.api.dto.param.link;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.cakk.common.enums.LinkKind;

public record ShopLinkParam(
	@NotBlank
	LinkKind linkKind,
	@NotBlank @Size(min = 1, max = 200)
	String linkPath
) {
}

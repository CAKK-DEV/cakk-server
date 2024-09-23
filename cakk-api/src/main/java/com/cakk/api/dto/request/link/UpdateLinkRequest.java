package com.cakk.api.dto.request.link;

import jakarta.validation.constraints.Size;

public record UpdateLinkRequest(
	@Size(min = 1, max = 200)
	String instagram,
	@Size(min = 1, max = 200)
	String kakao,
	@Size(min = 1, max = 200)
	String web
) {
}

package com.cakk.api.dto.request.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UpdateShopRequest(
	@Size(max = 200)
	String thumbnailUrl,
	@NotBlank @Size(max = 30)
	String shopName,
	@Size(max = 40)
	String shopBio,
	@Size(max = 500)
	String shopDescription
) {
}

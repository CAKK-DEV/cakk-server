package com.cakk.api.dto.response.cake;

import java.util.Set;

import lombok.Builder;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.mysql.dto.param.tag.TagParam;


@Builder
public record CakeDetailResponse(
	String cakeImageUrl,
	String cakeShopName,
	String shopBio,
	Long cakeShopId,
	Set<CakeDesignCategory> cakeCategories,
	Set<TagParam> tags
) {
}

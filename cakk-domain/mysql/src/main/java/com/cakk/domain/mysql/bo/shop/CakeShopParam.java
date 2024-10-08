package com.cakk.domain.mysql.bo.shop;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class CakeShopParam {

	private Long cakeShopId;
	private String thumbnailUrl;
	private String cakeShopName;
	private String cakeShopBio;
	private Set<String> cakeImageUrls;


	public void setImageCountMaxCount(final int maxCount) {
		cakeImageUrls = cakeImageUrls.stream().limit(maxCount).collect(Collectors.toSet());
	}

	public void setImagesEmptySet() {
		cakeImageUrls = new HashSet<>();
	}
}


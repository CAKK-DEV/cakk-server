package com.cakk.infrastructure.persistence.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.cake.Tag;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagMapper {

	public static Tag supplyTagBy(String tagName) {
		return Tag.builder()
			.tagName(tagName)
			.build();
	}
}

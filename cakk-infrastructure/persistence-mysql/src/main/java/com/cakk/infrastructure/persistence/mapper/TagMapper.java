package com.cakk.infrastructure.persistence.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.cake.TagEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagMapper {

	public static TagEntity supplyTagBy(String tagName) {
		return TagEntity.builder()
			.tagName(tagName)
			.build();
	}
}

package com.cakk.domain.mysql.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.domain.mysql.entity.cake.Tag;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagMapper {

	public static Tag supplyTagBy(String tagName) {
		return Tag.builder()
			.tagName(tagName)
			.build();
	}
}

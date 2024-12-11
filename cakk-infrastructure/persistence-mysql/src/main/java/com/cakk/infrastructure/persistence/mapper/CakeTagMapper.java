package com.cakk.infrastructure.persistence.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.cake.CakeEntity;
import com.cakk.infrastructure.persistence.entity.cake.CakeTagEntity;
import com.cakk.infrastructure.persistence.entity.cake.TagEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeTagMapper {

	public static CakeTagEntity supplyCakeTagBy(CakeEntity cake, TagEntity tag) {
		return CakeTagEntity.builder()
			.cake(cake)
			.tag(tag)
			.build();
	}
}

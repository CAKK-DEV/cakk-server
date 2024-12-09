package com.cakk.infrastructure.persistence.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.cake.Cake;
import com.cakk.infrastructure.persistence.entity.cake.CakeTag;
import com.cakk.infrastructure.persistence.entity.cake.Tag;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeTagMapper {

	public static CakeTag supplyCakeTagBy(Cake cake, Tag tag) {
		return CakeTag.builder()
			.cake(cake)
			.tag(tag)
			.build();
	}
}

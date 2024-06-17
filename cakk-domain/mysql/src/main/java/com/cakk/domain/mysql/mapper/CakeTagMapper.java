package com.cakk.domain.mysql.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeTag;
import com.cakk.domain.mysql.entity.cake.Tag;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeTagMapper {

	public static CakeTag supplyCakeTagBy(Cake cake, Tag tag) {
		return CakeTag.builder()
			.cake(cake)
			.tag(tag)
			.build();
	}
}

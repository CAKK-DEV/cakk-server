package com.cakk.domain.mysql.repository.writer;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.cake.Tag;
import com.cakk.domain.mysql.mapper.TagMapper;
import com.cakk.domain.mysql.repository.jpa.TagJpaRepository;

@Writer
@RequiredArgsConstructor
public class TagWriter {

	private final TagJpaRepository tagJpaRepository;

	public Tag saveTag(final String tagName) {
		return tagJpaRepository.save(TagMapper.supplyTagBy(tagName));
	}
}

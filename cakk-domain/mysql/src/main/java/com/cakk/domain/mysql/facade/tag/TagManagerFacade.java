package com.cakk.domain.mysql.facade.tag;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.DomainFacade;
import com.cakk.domain.mysql.entity.cake.Tag;
import com.cakk.domain.mysql.mapper.TagMapper;
import com.cakk.domain.mysql.repository.jpa.TagJpaRepository;

@RequiredArgsConstructor
@DomainFacade
public class TagManagerFacade {

	private final TagJpaRepository tagJpaRepository;

	public Tag saveIfNew(final String tagName) {
		return tagJpaRepository.findTagByTagName(tagName).orElseGet(
			() -> tagJpaRepository.save(TagMapper.supplyTagBy(tagName))
		);
	}
}

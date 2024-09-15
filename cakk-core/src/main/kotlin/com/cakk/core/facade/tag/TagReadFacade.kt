package com.cakk.core.facade.tag

import com.cakk.core.annotation.DomainFacade
import com.cakk.domain.mysql.entity.cake.Tag
import com.cakk.domain.mysql.mapper.TagMapper
import com.cakk.domain.mysql.repository.jpa.TagJpaRepository

@DomainFacade
class TagReadFacade(
	private val tagJpaRepository: TagJpaRepository
) {

    fun getTagsByTagName(tagNames: List<String>): List<Tag> {
        val tags: List<Tag> = tagJpaRepository.findTagsByTagNameIsIn(tagNames)
        return tagNames.stream()
                .map { tagName: String ->
                    tags
						.stream()
						.filter { tag: Tag -> tag.tagName == tagName }
						.findAny()
						.orElse(tagJpaRepository.save(TagMapper.supplyTagBy(tagName)))
                }.toList()
    }
}

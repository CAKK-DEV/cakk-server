package com.cakk.core.facade.tag

import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.entity.cake.Tag
import com.cakk.infrastructure.persistence.mapper.TagMapper
import com.cakk.infrastructure.persistence.repository.jpa.TagJpaRepository

@DomainFacade
class TagReadFacade(
	private val tagJpaRepository: TagJpaRepository
) {

    fun getTagsByTagName(tagNames: List<String>): List<Tag> {
        val tags: List<Tag> = tagJpaRepository.findTagsByTagNameIsIn(tagNames)
		return tagNames.map {
			tags.find {
				tag: Tag -> tag.tagName == it
			} ?: tagJpaRepository.save(TagMapper.supplyTagBy(it))
		}.toList()
    }
}


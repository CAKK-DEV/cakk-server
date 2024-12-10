package com.cakk.core.facade.tag

import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.entity.cake.TagEntity
import com.cakk.infrastructure.persistence.mapper.TagMapper
import com.cakk.infrastructure.persistence.repository.jpa.TagJpaRepository

@DomainFacade
class TagReadFacade(
	private val tagJpaRepository: TagJpaRepository
) {

    fun getTagsByTagName(tagNames: List<String>): List<TagEntity> {
        val tags: List<TagEntity> = tagJpaRepository.findTagsByTagNameIsIn(tagNames)
		return tagNames.map {
			tags.find {
				tag: TagEntity -> tag.tagName == it
			} ?: tagJpaRepository.save(TagMapper.supplyTagBy(it))
		}.toList()
    }
}


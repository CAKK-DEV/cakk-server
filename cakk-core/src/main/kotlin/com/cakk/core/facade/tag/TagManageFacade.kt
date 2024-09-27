package com.cakk.core.facade.tag

import com.cakk.core.annotation.DomainFacade
import com.cakk.domain.mysql.entity.cake.Tag
import com.cakk.domain.mysql.mapper.TagMapper
import com.cakk.domain.mysql.repository.jpa.TagJpaRepository

@DomainFacade
class TagManageFacade(
	private val tagJpaRepository: TagJpaRepository
) {

	fun create(tagName: String): Tag {
		return tagJpaRepository.findTagByTagName(tagName) ?: tagJpaRepository.save(TagMapper.supplyTagBy(tagName))
	}

	fun createAll(tagNames: List<String>): List<Tag> {
		val tags = tagJpaRepository.findTagsByTagNameIsIn(tagNames)

		return tagNames.map {
			tags.find { tag: Tag -> tag.tagName == it } ?: tagJpaRepository.save(TagMapper.supplyTagBy(it))
		}.toList()
	}
}

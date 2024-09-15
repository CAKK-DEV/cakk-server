package com.cakk.core.facade.tag

import com.cakk.domain.mysql.annotation.Reader
import com.cakk.domain.mysql.entity.cake.Tag

class TagReader {
    private val tagJpaRepository: TagJpaRepository? = null
    fun getTagsByTagName(tagNames: List<String>): List<Tag> {
        val tags: List<Tag> = tagJpaRepository.findTagsByTagNameIsIn(tagNames)
        return tagNames.stream()
                .map<Tag> { tagName: String ->
                    tags
                            .stream()
                            .filter { tag: Tag -> tag.tagName == tagName }
                            .findAny()
                            .orElse(tagJpaRepository.save<Tag>(TagMapper.supplyTagBy(tagName)))
                }
                .toList()
    }
}

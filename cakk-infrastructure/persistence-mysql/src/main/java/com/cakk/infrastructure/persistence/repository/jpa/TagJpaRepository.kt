package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.cake.TagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagJpaRepository : JpaRepository<TagEntity, Long> {

	fun findTagByTagName(tagName: String?): TagEntity?
    fun findTagsByTagNameIsIn(tagNames: List<String>): List<TagEntity>
}


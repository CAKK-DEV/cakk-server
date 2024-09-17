package com.cakk.domain.mysql.repository.jpa

import com.cakk.domain.mysql.entity.cake.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TagJpaRepository : JpaRepository<Tag, Long> {

	fun findTagByTagName(tagName: String?): Tag?
    fun findTagsByTagNameIsIn(tagNames: List<String>): List<Tag>
}


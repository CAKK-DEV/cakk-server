package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.cake.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TagJpaRepository : JpaRepository<com.cakk.infrastructure.persistence.entity.cake.Tag, Long> {

	fun findTagByTagName(tagName: String?): com.cakk.infrastructure.persistence.entity.cake.Tag?
    fun findTagsByTagNameIsIn(tagNames: List<String>): List<com.cakk.infrastructure.persistence.entity.cake.Tag>
}


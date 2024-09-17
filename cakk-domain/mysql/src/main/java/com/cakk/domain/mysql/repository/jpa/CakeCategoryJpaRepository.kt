package com.cakk.domain.mysql.repository.jpa

import com.cakk.domain.mysql.entity.cake.CakeCategory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CakeCategoryJpaRepository : JpaRepository<CakeCategory, Long> {

	fun findByCakeId(cakeId: Long?): CakeCategory?
}


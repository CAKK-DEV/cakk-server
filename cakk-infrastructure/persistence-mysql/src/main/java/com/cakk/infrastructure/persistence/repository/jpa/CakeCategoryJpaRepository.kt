package com.cakk.infrastructure.persistence.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import com.cakk.infrastructure.persistence.entity.cake.CakeCategory

interface CakeCategoryJpaRepository : JpaRepository<CakeCategory, Long> {

	fun findByCakeId(cakeId: Long?): CakeCategory?
}


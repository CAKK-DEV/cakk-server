package com.cakk.infrastructure.persistence.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import com.cakk.infrastructure.persistence.entity.cake.CakeCategoryEntity

interface CakeCategoryJpaRepository : JpaRepository<CakeCategoryEntity, Long> {

	fun findByCakeId(cakeId: Long?): CakeCategoryEntity?
}


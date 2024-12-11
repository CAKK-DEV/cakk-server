package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.shop.CakeShopOperationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CakeShopOperationJpaRepository : JpaRepository<CakeShopOperationEntity, Long> {

	fun findAllByCakeShopId(cakeShopId: Long): List<CakeShopOperationEntity>
}


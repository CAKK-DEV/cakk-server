package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation
import org.springframework.data.jpa.repository.JpaRepository

interface CakeShopOperationJpaRepository : JpaRepository<com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation, Long> {

	fun findAllByCakeShopId(cakeShopId: Long): List<com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation>
}


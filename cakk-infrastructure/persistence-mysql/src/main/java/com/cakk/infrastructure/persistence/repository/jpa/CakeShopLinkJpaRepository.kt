package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.shop.CakeShopLinkEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CakeShopLinkJpaRepository : JpaRepository<CakeShopLinkEntity, Long> {

	fun findAllByCakeShopId(cakeShopId: Long): List<CakeShopLinkEntity>
}


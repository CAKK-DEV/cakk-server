package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.shop.CakeShopLink
import org.springframework.data.jpa.repository.JpaRepository

interface CakeShopLinkJpaRepository : JpaRepository<com.cakk.infrastructure.persistence.entity.shop.CakeShopLink, Long> {

	fun findAllByCakeShopId(cakeShopId: Long): List<com.cakk.infrastructure.persistence.entity.shop.CakeShopLink>
}


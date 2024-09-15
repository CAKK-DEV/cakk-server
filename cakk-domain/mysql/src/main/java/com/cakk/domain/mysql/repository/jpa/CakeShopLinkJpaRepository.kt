package com.cakk.domain.mysql.repository.jpa

import com.cakk.domain.mysql.entity.shop.CakeShopLink
import org.springframework.data.jpa.repository.JpaRepository

interface CakeShopLinkJpaRepository : JpaRepository<CakeShopLink, Long> {
    fun findAllByCakeShopId(cakeShopId: Long): List<CakeShopLink>
}

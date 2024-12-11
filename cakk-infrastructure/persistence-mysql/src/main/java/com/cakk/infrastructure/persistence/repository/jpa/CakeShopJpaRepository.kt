package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CakeShopJpaRepository : JpaRepository<CakeShopEntity, Long>

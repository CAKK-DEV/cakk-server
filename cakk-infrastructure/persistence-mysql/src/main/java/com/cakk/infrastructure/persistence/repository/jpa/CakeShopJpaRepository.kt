package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.shop.CakeShop
import org.springframework.data.jpa.repository.JpaRepository

interface CakeShopJpaRepository : JpaRepository<com.cakk.infrastructure.persistence.entity.shop.CakeShop, Long>

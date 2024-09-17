package com.cakk.domain.mysql.repository.jpa

import com.cakk.domain.mysql.entity.shop.CakeShop
import org.springframework.data.jpa.repository.JpaRepository

interface CakeShopJpaRepository : JpaRepository<CakeShop, Long>

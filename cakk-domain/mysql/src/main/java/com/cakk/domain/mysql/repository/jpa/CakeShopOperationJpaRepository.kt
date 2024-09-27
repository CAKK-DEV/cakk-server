package com.cakk.domain.mysql.repository.jpa

import com.cakk.domain.mysql.entity.shop.CakeShopOperation
import org.springframework.data.jpa.repository.JpaRepository

interface CakeShopOperationJpaRepository : JpaRepository<CakeShopOperation, Long> {

	fun findAllByCakeShopId(cakeShopId: Long): List<CakeShopOperation>
}


package com.cakk.domain.mysql.repository.jpa

import com.cakk.domain.mysql.entity.shop.CakeShopLike
import com.cakk.domain.mysql.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface CakeShopLikeJpaRepository : JpaRepository<CakeShopLike, Long> {
    fun countByCakeShopIdAndUser(cakeShopId: Long, user: User): Int
}

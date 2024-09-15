package com.cakk.domain.mysql.repository.jpa

import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.shop.CakeShopHeart
import com.cakk.domain.mysql.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CakeShopHeartJpaRepository : JpaRepository<CakeShopHeart, Long> {
    fun findAllByUser(user: User): List<CakeShopHeart>
    fun findByUserAndCakeShop(user: User, cakeShop: CakeShop): CakeShopHeart?
    fun existsByUserAndCakeShop(user: User, cakeShop: CakeShop): Boolean
}

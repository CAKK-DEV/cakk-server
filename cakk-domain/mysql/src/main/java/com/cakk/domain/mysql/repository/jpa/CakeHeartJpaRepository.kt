package com.cakk.domain.mysql.repository.jpa

import com.cakk.domain.mysql.entity.cake.Cake
import com.cakk.domain.mysql.entity.cake.CakeHeart
import com.cakk.domain.mysql.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CakeHeartJpaRepository : JpaRepository<CakeHeart, Long> {

	fun findAllByUser(user: User): List<CakeHeart>
    fun findByUserAndCake(user: User, cake: Cake): CakeHeart?
    fun existsByUserAndCake(user: User, cake: Cake): Boolean
}


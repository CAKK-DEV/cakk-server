package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.cake.Cake
import com.cakk.infrastructure.persistence.entity.cake.CakeHeart
import com.cakk.infrastructure.persistence.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CakeHeartJpaRepository : JpaRepository<com.cakk.infrastructure.persistence.entity.cake.CakeHeart, Long> {

	fun findAllByUser(user: com.cakk.infrastructure.persistence.entity.user.User): List<com.cakk.infrastructure.persistence.entity.cake.CakeHeart>
    fun findByUserAndCake(user: com.cakk.infrastructure.persistence.entity.user.User, cake: com.cakk.infrastructure.persistence.entity.cake.Cake): com.cakk.infrastructure.persistence.entity.cake.CakeHeart?
    fun existsByUserAndCake(user: com.cakk.infrastructure.persistence.entity.user.User, cake: com.cakk.infrastructure.persistence.entity.cake.Cake): Boolean
}


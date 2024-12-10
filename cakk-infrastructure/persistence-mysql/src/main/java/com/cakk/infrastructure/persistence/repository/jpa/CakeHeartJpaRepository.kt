package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.cake.CakeEntity
import com.cakk.infrastructure.persistence.entity.cake.CakeHeartEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CakeHeartJpaRepository : JpaRepository<CakeHeartEntity, Long> {

	fun findAllByUser(userEntity: UserEntity): List<CakeHeartEntity>
    fun findByUserAndCake(userEntity: UserEntity, cake: CakeEntity): CakeHeartEntity?
    fun existsByUserAndCake(userEntity: UserEntity, cake: CakeEntity): Boolean
}


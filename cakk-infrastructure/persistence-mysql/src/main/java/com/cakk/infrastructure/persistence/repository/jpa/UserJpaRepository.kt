package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, Long> {

    fun findByProviderId(providerId: String): User?
}


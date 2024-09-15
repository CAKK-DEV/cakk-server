package com.cakk.domain.mysql.repository.jpa

import com.cakk.domain.mysql.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserJpaRepository : JpaRepository<User, Long> {
    fun findByProviderId(providerId: String): User?
}

package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.user.UserWithdrawalEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserWithdrawalJpaRepository : JpaRepository<UserWithdrawalEntity, Long>

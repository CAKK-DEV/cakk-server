package com.cakk.infrastructure.persistence.repository.jpa

import com.cakk.infrastructure.persistence.entity.user.UserWithdrawal
import org.springframework.data.jpa.repository.JpaRepository

interface UserWithdrawalJpaRepository : JpaRepository<com.cakk.infrastructure.persistence.entity.user.UserWithdrawal, Long>

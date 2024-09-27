package com.cakk.domain.mysql.repository.jpa

import com.cakk.domain.mysql.entity.user.UserWithdrawal
import org.springframework.data.jpa.repository.JpaRepository

interface UserWithdrawalJpaRepository : JpaRepository<UserWithdrawal, Long>

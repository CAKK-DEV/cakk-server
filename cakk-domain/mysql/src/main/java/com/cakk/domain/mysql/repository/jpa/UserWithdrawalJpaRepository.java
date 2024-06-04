package com.cakk.domain.mysql.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.user.UserWithdrawal;

public interface UserWithdrawalJpaRepository extends JpaRepository<UserWithdrawal, Long> {
}

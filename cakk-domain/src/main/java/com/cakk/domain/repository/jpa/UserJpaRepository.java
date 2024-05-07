package com.cakk.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.entity.user.User;

public interface UserJpaRepository extends JpaRepository<User, Long> { }

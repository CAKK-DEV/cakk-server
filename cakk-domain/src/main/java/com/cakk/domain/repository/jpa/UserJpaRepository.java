package com.cakk.domain.repository.jpa;

import com.cakk.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> { }

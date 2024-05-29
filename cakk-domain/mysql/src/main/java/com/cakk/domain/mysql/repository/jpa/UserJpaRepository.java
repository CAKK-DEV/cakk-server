package com.cakk.domain.mysql.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.user.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

	Optional<User> findByProviderId(String providerId);
}

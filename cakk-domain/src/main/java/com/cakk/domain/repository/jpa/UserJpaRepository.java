package com.cakk.domain.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.entity.user.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

	Optional<User> findByProviderId(String providerId);
}

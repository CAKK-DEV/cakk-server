package com.cakk.domain.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.entity.cake.CakeCategory;

public interface CakeCategoryJpaRepository extends JpaRepository<CakeCategory, Long> {

	Optional<CakeCategory> findByCakeId(Long cakeId);
}

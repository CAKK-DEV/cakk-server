package com.cakk.domain.mysql.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.cake.CakeCategory;

public interface CakeCategoryJpaRepository extends JpaRepository<CakeCategory, Long> {

	Optional<CakeCategory> findByCakeId(Long cakeId);
}

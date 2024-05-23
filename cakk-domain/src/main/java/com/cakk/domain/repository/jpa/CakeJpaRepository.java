package com.cakk.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.entity.cake.Cake;

public interface CakeJpaRepository extends JpaRepository<Cake, Long> {
}

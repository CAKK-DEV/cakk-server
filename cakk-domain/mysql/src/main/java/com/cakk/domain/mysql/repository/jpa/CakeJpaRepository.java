package com.cakk.domain.mysql.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.cake.Cake;

public interface CakeJpaRepository extends JpaRepository<Cake, Long> {
}

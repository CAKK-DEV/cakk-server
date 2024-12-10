package com.cakk.infrastructure.persistence.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository

import com.cakk.infrastructure.persistence.entity.cake.CakeEntity

interface CakeJpaRepository : JpaRepository<CakeEntity, Long>

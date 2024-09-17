package com.cakk.domain.mysql.repository.jpa

import com.cakk.domain.mysql.entity.cake.Cake
import org.springframework.data.jpa.repository.JpaRepository

interface CakeJpaRepository : JpaRepository<Cake, Long>

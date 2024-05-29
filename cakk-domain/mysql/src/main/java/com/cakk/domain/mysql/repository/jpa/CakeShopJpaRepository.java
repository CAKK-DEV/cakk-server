package com.cakk.domain.mysql.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.shop.CakeShop;

public interface CakeShopJpaRepository extends JpaRepository<CakeShop, Long> {
}

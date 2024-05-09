package com.cakk.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.entity.shop.CakeShop;

public interface CakeShopJpaRepository extends JpaRepository<CakeShop, Long> {
}

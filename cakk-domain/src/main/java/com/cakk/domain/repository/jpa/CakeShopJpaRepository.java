package com.cakk.domain.repository.jpa;

import com.cakk.domain.entity.shop.CakeShop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CakeShopJpaRepository extends JpaRepository<CakeShop, Long> {
}

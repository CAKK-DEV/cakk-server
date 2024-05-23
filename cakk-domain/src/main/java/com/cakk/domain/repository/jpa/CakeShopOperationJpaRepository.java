package com.cakk.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.entity.shop.CakeShopOperation;

public interface CakeShopOperationJpaRepository extends JpaRepository<CakeShopOperation, Long> { }

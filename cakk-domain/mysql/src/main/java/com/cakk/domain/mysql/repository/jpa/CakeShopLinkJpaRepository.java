package com.cakk.domain.mysql.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.shop.CakeShopLink;

public interface CakeShopLinkJpaRepository extends JpaRepository<CakeShopLink, Long> {

	List<CakeShopLink> findAllByCakeShopId(Long cakeShopId);
}

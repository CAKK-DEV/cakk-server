package com.cakk.domain.mysql.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.shop.CakeShopLike;
import com.cakk.domain.mysql.entity.user.User;

public interface CakeShopLikeJpaRepository extends JpaRepository<CakeShopLike, Long> {

	int countByCakeShopIdAndUser(Long cakeShopId, User user);
}

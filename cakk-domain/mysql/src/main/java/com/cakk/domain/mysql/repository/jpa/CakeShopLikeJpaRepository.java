package com.cakk.domain.mysql.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.shop.CakeShopLike;
import com.cakk.domain.mysql.entity.user.User;

public interface CakeShopLikeJpaRepository extends JpaRepository<CakeShopLike, Long> {

	List<CakeShopLike> findAllByUser(User user);
}

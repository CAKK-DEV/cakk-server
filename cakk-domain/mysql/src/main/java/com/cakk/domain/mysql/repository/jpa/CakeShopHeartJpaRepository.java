package com.cakk.domain.mysql.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopHeart;
import com.cakk.domain.mysql.entity.user.User;

public interface CakeShopHeartJpaRepository extends JpaRepository<CakeShopHeart, Long> {

	List<CakeShopHeart> findAllByUser(User user);

	Optional<CakeShopHeart> findByUserAndCakeShop(User user, CakeShop cakeShop);

	boolean existsByUserAndCakeShop(User user, CakeShop cakeShop);
}

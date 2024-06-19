package com.cakk.domain.mysql.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;

public interface CakeShopJpaRepository extends JpaRepository<CakeShop, Long> {

	Optional<CakeShop> findCakeShopByIdAndBusinessInformation_User(Long cakeShopId, User owner);
}

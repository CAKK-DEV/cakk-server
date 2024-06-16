package com.cakk.domain.mysql.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;

public interface CakeShopJpaRepository extends JpaRepository<CakeShop, Long> {

	@Query(value = "select CS from CakeShop as CS where ST_CONTAINS(ST_BUFFER(:point, 5000), CS.location)")
	List<CakeShop> findByLocationBased(Point point);

	Optional<CakeShop> findCakeShopByIdAndBusinessInformation_User(Long cakeShopId, User owner);
}

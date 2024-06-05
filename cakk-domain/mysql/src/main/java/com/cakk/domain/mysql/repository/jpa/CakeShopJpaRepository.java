package com.cakk.domain.mysql.repository.jpa;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cakk.domain.mysql.entity.shop.CakeShop;

public interface CakeShopJpaRepository extends JpaRepository<CakeShop, Long> {

	@Query(value = "select CS from CakeShop as CS where ST_CONTAINS(ST_BUFFER(:point, 5000), CS.location)")
	List<CakeShop> findByLocationBased(Point point);
}

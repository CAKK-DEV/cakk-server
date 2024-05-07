package com.cakk.domain.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cakk.domain.entity.user.BusinessInformation;

public interface BusinessInformationJpaRepository extends JpaRepository<BusinessInformation, Long> {

	@Query("select bi from BusinessInformation as bi join fetch bi.cakeShop where bi.cakeShop.id =:cakeShopId")
	Optional<BusinessInformation> findBusinessInformationByCakeShopId(Long cakeShopId);
}

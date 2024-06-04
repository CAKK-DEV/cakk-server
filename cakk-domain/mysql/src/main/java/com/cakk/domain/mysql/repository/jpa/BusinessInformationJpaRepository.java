package com.cakk.domain.mysql.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;

public interface BusinessInformationJpaRepository extends JpaRepository<BusinessInformation, Long> {

	@Query("select bi from BusinessInformation as bi join fetch bi.cakeShop "
		+ "where bi.cakeShop.id =:cakeShopId")
	Optional<BusinessInformation> findBusinessInformationWithCakeShop(@Param("cakeShopId") Long cakeShopId);

	Optional<BusinessInformation> findBusinessInformationByCakeShopId(Long cakeShopId);

	List<BusinessInformation> findAllByUser(User user);
}

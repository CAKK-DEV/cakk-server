package com.cakk.domain.mysql.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cakk.common.enums.VerificationStatus;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;

public interface BusinessInformationJpaRepository extends JpaRepository<BusinessInformation, Long> {

	@Query("select bi from BusinessInformation as bi join fetch bi.cakeShop "
		+ "where bi.cakeShop.id =:cakeShopId")
	Optional<BusinessInformation> findBusinessInformationWithCakeShop(@Param("cakeShopId") Long cakeShopId);

	List<BusinessInformation> findAllByUser(User user);

	Boolean existsBusinessInformationByUserAndCakeShop_Id(User owner, Long cakeShopId);

	@Query("select bi from BusinessInformation as bi join fetch bi.cakeShop where bi.user =:user")
	List<BusinessInformation> findAllWithCakeShopByUser(User user);

	@Query("select bi from BusinessInformation as bi join fetch bi.user join fetch bi.cakeShop"
		+ " where bi.verificationStatus =:verificationStatus")
	List<BusinessInformation> findAllCakeShopBusinessOwnerCandidates(VerificationStatus verificationStatus);

	@Query("select bi from BusinessInformation as bi join fetch bi.user join fetch bi.cakeShop"
		+ " where bi.user.id =:userId")
	Optional<BusinessInformation> findBusinessInformationByUserId(final Long userId);
}

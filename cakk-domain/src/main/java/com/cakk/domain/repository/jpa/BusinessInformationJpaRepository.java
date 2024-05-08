package com.cakk.domain.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cakk.domain.entity.user.BusinessInformation;

public interface BusinessInformationJpaRepository extends JpaRepository<BusinessInformation, Long> {

	Optional<BusinessInformation> findBusinessInformationByCakeShopId(Long cakeShopId);

}

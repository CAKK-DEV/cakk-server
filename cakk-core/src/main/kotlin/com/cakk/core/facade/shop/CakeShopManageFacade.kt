package com.cakk.core.facade.shop

import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLinkEntity
import com.cakk.infrastructure.persistence.entity.shop.CakeShopOperationEntity
import com.cakk.infrastructure.persistence.entity.user.BusinessInformationEntity
import com.cakk.infrastructure.persistence.repository.jpa.CakeShopJpaRepository

@DomainFacade
class CakeShopManageFacade(
	private val cakeShopJpaRepository: CakeShopJpaRepository
) {

	fun create(
		cakeShop: CakeShopEntity,
		cakeShopOperations: List<CakeShopOperationEntity>,
		businessInformationEntity: BusinessInformationEntity,
		cakeShopLinks: List<CakeShopLinkEntity>
	): CakeShopEntity {
		cakeShop.addShopOperationDays(cakeShopOperations)
		cakeShop.addShopLinks(cakeShopLinks)
		cakeShop.registerBusinessInformation(businessInformationEntity)

		return cakeShopJpaRepository.save(cakeShop)
	}
}

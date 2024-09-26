package com.cakk.core.facade.shop

import com.cakk.core.annotation.DomainFacade
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.shop.CakeShopLink
import com.cakk.domain.mysql.entity.shop.CakeShopOperation
import com.cakk.domain.mysql.entity.user.BusinessInformation
import com.cakk.domain.mysql.repository.jpa.CakeShopJpaRepository

@DomainFacade
class CakeShopManageFacade(
	private val cakeShopJpaRepository: CakeShopJpaRepository
) {

	fun create(
		cakeShop: CakeShop,
		cakeShopOperations: List<CakeShopOperation>,
		businessInformation: BusinessInformation,
		cakeShopLinks: List<CakeShopLink>
	): CakeShop {
		cakeShop.addShopOperationDays(cakeShopOperations)
		cakeShop.addShopLinks(cakeShopLinks)
		cakeShop.registerBusinessInformation(businessInformation)

		return cakeShopJpaRepository.save(cakeShop)
	}
}

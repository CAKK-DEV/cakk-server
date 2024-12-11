package com.cakk.core.facade.cake

import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.entity.cake.CakeEntity
import com.cakk.infrastructure.persistence.entity.cake.CakeCategoryEntity
import com.cakk.infrastructure.persistence.entity.cake.TagEntity
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import com.cakk.infrastructure.persistence.repository.jpa.CakeJpaRepository

@DomainFacade
class CakeManageFacade(
	private val cakeJpaRepository: CakeJpaRepository
) {


    fun create(cakeShop: CakeShopEntity, cake: CakeEntity, tags: List<TagEntity>, cakeCategories: List<CakeCategoryEntity>) {
        cake.registerTags(tags)
        cake.registerCategories(cakeCategories)
        cakeShop.registerCake(cake)

		cakeJpaRepository.save(cake)
    }

    fun update(cake: CakeEntity, cakeImageUrl: String, tags: List<TagEntity>, cakeCategories: List<CakeCategoryEntity>) {
        cake.updateCakeImageUrl(cakeImageUrl)
        cake.updateCakeCategories(cakeCategories)
		cake.updateCakeTags(tags)
    }

    fun delete(cake: CakeEntity) {
        cakeJpaRepository.delete(cake)
    }
}

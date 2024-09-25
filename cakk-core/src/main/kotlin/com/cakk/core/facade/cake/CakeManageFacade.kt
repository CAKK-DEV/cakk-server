package com.cakk.core.facade.cake

import com.cakk.core.annotation.DomainFacade
import com.cakk.domain.mysql.entity.cake.Cake
import com.cakk.domain.mysql.entity.cake.CakeCategory
import com.cakk.domain.mysql.entity.cake.Tag
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.repository.jpa.CakeJpaRepository

@DomainFacade
class CakeManageFacade(
	private val cakeJpaRepository: CakeJpaRepository
) {


    fun create(cakeShop: CakeShop, cake: Cake, tags: List<Tag>, cakeCategories: List<CakeCategory>) {
        cake.registerTags(tags)
        cake.registerCategories(cakeCategories)
        cakeShop.registerCake(cake)
    }

    fun update(cake: Cake, cakeImageUrl: String, tags: List<Tag>, cakeCategories: List<CakeCategory>) {
        cake.updateCakeImageUrl(cakeImageUrl)
        cake.updateCakeCategories(cakeCategories)
		cake.updateCakeTags(tags)
    }

    fun delete(cake: Cake) {
        cakeJpaRepository.delete(cake)
    }
}

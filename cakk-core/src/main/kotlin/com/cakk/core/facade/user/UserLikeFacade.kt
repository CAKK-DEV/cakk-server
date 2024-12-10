package com.cakk.core.facade.user

import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity

@DomainFacade
class UserLikeFacade {

    fun likeCakeShop(userEntity: UserEntity, cakeShop: CakeShopEntity) {
        userEntity.likeCakeShop(cakeShop)
    }
}

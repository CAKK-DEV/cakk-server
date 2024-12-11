package com.cakk.core.facade.user

import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.entity.cake.CakeEntity
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity

@DomainFacade
class UserHeartFacade {

    fun heartCake(userEntity: UserEntity, cake: CakeEntity) {
        if (!cake.isHeartedBy(userEntity)) {
            userEntity.heartCake(cake)
        } else {
            userEntity.unHeartCake(cake)
        }
    }

    fun heartCakeShop(userEntity: UserEntity, cakeShop: CakeShopEntity) {
        if (!cakeShop.isHeartedBy(userEntity)) {
            userEntity.heartCakeShop(cakeShop)
        } else {
            userEntity.unHeartCakeShop(cakeShop)
        }
    }
}

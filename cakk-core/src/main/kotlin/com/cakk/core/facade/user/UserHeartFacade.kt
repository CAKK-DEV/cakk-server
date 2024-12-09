package com.cakk.core.facade.user

import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.entity.cake.Cake
import com.cakk.infrastructure.persistence.entity.shop.CakeShop
import com.cakk.infrastructure.persistence.entity.user.User

@DomainFacade
class UserHeartFacade {

    fun heartCake(user: com.cakk.infrastructure.persistence.entity.user.User, cake: com.cakk.infrastructure.persistence.entity.cake.Cake) {
        if (!cake.isHeartedBy(user)) {
            user.heartCake(cake)
        } else {
            user.unHeartCake(cake)
        }
    }

    fun heartCakeShop(user: com.cakk.infrastructure.persistence.entity.user.User, cakeShop: com.cakk.infrastructure.persistence.entity.shop.CakeShop) {
        if (!cakeShop.isHeartedBy(user)) {
            user.heartCakeShop(cakeShop)
        } else {
            user.unHeartCakeShop(cakeShop)
        }
    }
}

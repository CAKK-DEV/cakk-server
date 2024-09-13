package com.cakk.core.facade.user

import com.cakk.core.annotation.DomainFacade
import com.cakk.domain.mysql.entity.cake.Cake
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.user.User

@DomainFacade
class UserHeartFacade {

    fun heartCake(user: User, cake: Cake) {
        if (!cake.isHeartedBy(user)) {
            user.heartCake(cake)
        } else {
            user.unHeartCake(cake)
        }
    }

    fun heartCakeShop(user: User, cakeShop: CakeShop) {
        if (!cakeShop.isHeartedBy(user)) {
            user.heartCakeShop(cakeShop)
        } else {
            user.unHeartCakeShop(cakeShop)
        }
    }
}

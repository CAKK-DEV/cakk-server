package com.cakk.core.facade.user

import com.cakk.core.annotation.DomainFacade
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.user.User

@DomainFacade
class UserLikeFacade {

    fun likeCakeShop(user: User, cakeShop: CakeShop) {
        user.likeCakeShop(cakeShop)
    }
}

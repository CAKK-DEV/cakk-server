package com.cakk.core.facade.user

import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.entity.shop.CakeShop
import com.cakk.infrastructure.persistence.entity.user.User

@DomainFacade
class UserLikeFacade {

    fun likeCakeShop(user: com.cakk.infrastructure.persistence.entity.user.User, cakeShop: com.cakk.infrastructure.persistence.entity.shop.CakeShop) {
        user.likeCakeShop(cakeShop)
    }
}

package com.cakk.admin.dto.param

import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.shop.CakeShopLink
import com.cakk.domain.mysql.entity.shop.CakeShopOperation
import com.cakk.domain.mysql.entity.user.BusinessInformation

data class CakeShopCreateByAdminParam(
    val cakeShop: CakeShop,
    val businessInformation: BusinessInformation,
    val cakeShopOperations: List<CakeShopOperation>,
    val cakeShopLinks: List<CakeShopLink>
)

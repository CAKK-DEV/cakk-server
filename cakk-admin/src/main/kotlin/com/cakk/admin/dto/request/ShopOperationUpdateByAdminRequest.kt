package com.cakk.admin.dto.request

import com.cakk.admin.mapper.supplyCakeShopOperationListBy
import com.cakk.domain.mysql.dto.param.operation.UpdateShopOperationParam
import com.cakk.domain.mysql.dto.param.shop.ShopOperationParam
import jakarta.validation.constraints.NotNull

data class ShopOperationUpdateByAdminRequest(
    @field:NotNull
    val operationDays: List<ShopOperationParam>
) {

    fun toParam(cakeShopId: Long): UpdateShopOperationParam {
        return UpdateShopOperationParam.builder()
            .cakeShopId(cakeShopId)
            .cakeShopOperations(supplyCakeShopOperationListBy(operationDays))
            .build()
    }
}

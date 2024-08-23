package com.cakk.admin.dto.request

import com.cakk.admin.annotation.OperationDay
import com.cakk.admin.dto.param.CakeShopCreateByAdminParam
import com.cakk.admin.dto.param.ShopLinkParam
import com.cakk.admin.mapper.supplyBusinessInformationBy
import com.cakk.admin.mapper.supplyCakeShopBy
import com.cakk.admin.mapper.supplyCakeShopLinksBy
import com.cakk.admin.mapper.supplyCakeShopOperationsBy
import com.cakk.domain.mysql.dto.param.shop.ShopOperationParam
import com.cakk.domain.mysql.entity.shop.CakeShop
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CakeShopCreateByAdminRequest(
    @field:Size(max = 20)
    val businessNumber: String,
    @field:OperationDay
    val operationDays: List<ShopOperationParam>,
    @field:NotBlank @field:Size(max = 30)
    val shopName: String,
    @field:Size(max = 40)
    val shopBio: String,
    @field:Size(max = 500)
    val shopDescription: String,
    @field:NotBlank @field:Size(max = 50)
    val shopAddress: String,
    @field:NotNull @field:Min(-90) @field:Max(90)
    val latitude: Double,
    @field:NotNull @field:Min(-180) @field:Max(180)
    val longitude: Double,
    @field:NotNull
    val links: List<ShopLinkParam>
) {

    fun toParam(): CakeShopCreateByAdminParam {
        val cakeShop: CakeShop = supplyCakeShopBy(this)

        return CakeShopCreateByAdminParam(
            cakeShop = cakeShop,
            businessInformation = supplyBusinessInformationBy(businessNumber, cakeShop),
            cakeShopOperations = supplyCakeShopOperationsBy(cakeShop, operationDays),
            cakeShopLinks = supplyCakeShopLinksBy(cakeShop, links)
        )
    }
}

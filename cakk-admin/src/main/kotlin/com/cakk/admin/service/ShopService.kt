package com.cakk.admin.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.cakk.admin.dto.param.CakeShopCreateByAdminParam
import com.cakk.admin.dto.response.CakeShopCreateResponse
import com.cakk.admin.mapper.supplyCakeShopCreateResponseBy
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.shop.CakeShopManageFacade
import com.cakk.domain.mysql.dto.param.link.UpdateLinkParam
import com.cakk.domain.mysql.dto.param.operation.UpdateShopOperationParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopUpdateParam
import com.cakk.domain.mysql.dto.param.shop.UpdateShopAddressParam
import com.cakk.domain.mysql.entity.shop.CakeShop

@Service
class ShopService(
    private val cakeShopReadFacade: CakeShopReadFacade,
	private val cakeShopManageFacade: CakeShopManageFacade
) {

    @Transactional
    fun createCakeShopByCertification(dto: CakeShopCreateByAdminParam): CakeShopCreateResponse {
        val result: CakeShop = cakeShopManageFacade.create(
            dto.cakeShop,
            dto.cakeShopOperations,
            dto.businessInformation,
            dto.cakeShopLinks
        )

        return supplyCakeShopCreateResponseBy(result)
    }

    @Transactional
    fun updateBasicInformation(dto: CakeShopUpdateParam) {
        val cakeShop = cakeShopReadFacade.findById(dto.cakeShopId)
        cakeShop.updateBasicInformation(dto)
    }

    @Transactional
    fun updateShopLinks(param: UpdateLinkParam) {
        val cakeShop = cakeShopReadFacade.findById(param.cakeShopId)
        cakeShop.updateShopLinks(param.cakeShopLinks)
    }

    @Transactional
    fun updateShopOperationDays(param: UpdateShopOperationParam) {
        val cakeShop = cakeShopReadFacade.findById(param.cakeShopId)
        cakeShop.updateShopOperationDays(param.cakeShopOperations)
    }

    @Transactional
    fun updateShopAddress(param: UpdateShopAddressParam) {
        val cakeShop = cakeShopReadFacade.findById(param.cakeShopId)
        cakeShop.updateShopAddress(param)
    }
}

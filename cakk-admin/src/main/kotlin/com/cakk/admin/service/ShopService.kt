package com.cakk.admin.service

import com.cakk.admin.dto.param.CakeShopCreateByAdminParam
import com.cakk.admin.dto.response.CakeShopCreateResponse
import com.cakk.admin.mapper.supplyCakeShopCreateResponseBy
import com.cakk.domain.mysql.dto.param.link.UpdateLinkParam
import com.cakk.domain.mysql.dto.param.operation.UpdateShopOperationParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopUpdateParam
import com.cakk.domain.mysql.dto.param.shop.UpdateShopAddressParam
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.repository.reader.CakeShopReader
import com.cakk.domain.mysql.repository.writer.CakeShopWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ShopService(
    private val cakeShopReader: CakeShopReader,
    private val cakeShopWriter: CakeShopWriter
) {

    @Transactional
    fun createCakeShopByCertification(dto: CakeShopCreateByAdminParam): CakeShopCreateResponse {
        val result: CakeShop = cakeShopWriter.createCakeShop(
            dto.cakeShop,
            dto.cakeShopOperations,
            dto.businessInformation,
            dto.cakeShopLinks
        )

        return supplyCakeShopCreateResponseBy(result)
    }

    @Transactional
    fun updateBasicInformation(dto: CakeShopUpdateParam) {
        val cakeShop = cakeShopReader.findById(dto.cakeShopId)
        cakeShop.updateBasicInformation(dto)
    }

    @Transactional
    fun updateShopLinks(param: UpdateLinkParam) {
        val cakeShop = cakeShopReader.findById(param.cakeShopId)
        cakeShop.updateShopLinks(param.cakeShopLinks)
    }

    @Transactional
    fun updateShopOperationDays(param: UpdateShopOperationParam) {
        val cakeShop = cakeShopReader.findById(param.cakeShopId)
        cakeShop.updateShopOperationDays(param.cakeShopOperations)
    }

    @Transactional
    fun updateShopAddress(param: UpdateShopAddressParam) {
        val cakeShop = cakeShopReader.findById(param.cakeShopId)
        cakeShop.updateShopAddress(param)
    }
}

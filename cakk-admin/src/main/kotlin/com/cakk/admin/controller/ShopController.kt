package com.cakk.admin.controller

import com.cakk.admin.dto.request.*
import com.cakk.admin.dto.response.CakeShopCreateResponse
import com.cakk.admin.service.ShopService
import com.cakk.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/shops")
class ShopController(
    private val shopService: ShopService
) {

    @PostMapping
    fun createByAdmin(
        @RequestBody @Valid request: CakeShopCreateByAdminRequest
    ): ApiResponse<CakeShopCreateResponse> {
        val response = shopService.createCakeShopByCertification(request.toParam())

        return ApiResponse.success(response)
    }

    @PutMapping("/{cakeShopId}")
    fun updateBasicInformation(
        @PathVariable cakeShopId: Long,
        @RequestBody @Valid request: CakeShopUpdateByAdminRequest
    ): ApiResponse<Unit> {
        shopService.updateBasicInformation(request.toParam(cakeShopId))

        return ApiResponse.success()
    }

    @PutMapping("/{cakeShopId}/links")
    fun updateLinks(
        @PathVariable cakeShopId: Long,
        @RequestBody request: @Valid LinkUpdateByAdminRequest
    ): ApiResponse<Unit> {
        shopService.updateShopLinks(request.toParam(cakeShopId))

        return ApiResponse.success()
    }

    @PutMapping("/{cakeShopId}/operation-days")
    fun updateOperationDays(
        @PathVariable cakeShopId: Long,
        @RequestBody @Valid request: ShopOperationUpdateByAdminRequest
    ): ApiResponse<Unit> {
        shopService.updateShopOperationDays(request.toParam(cakeShopId))

        return ApiResponse.success()
    }

    @PutMapping("/{cakeShopId}/address")
    fun updateAddress(
        @PathVariable cakeShopId: Long,
        @RequestBody @Valid request: AddressUpdateByAdminRequest
    ): ApiResponse<Unit> {
        shopService.updateShopAddress(request.toParam(cakeShopId))

        return ApiResponse.success()
    }
}

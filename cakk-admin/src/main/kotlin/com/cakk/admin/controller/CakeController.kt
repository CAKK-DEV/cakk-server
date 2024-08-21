package com.cakk.admin.controller

import com.cakk.admin.dto.request.CakeCreateByAdminRequest
import com.cakk.admin.dto.request.CakeUpdateByAdminRequest
import com.cakk.admin.dto.response.CakeDetailResponse
import com.cakk.admin.service.CakeService
import com.cakk.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/shops/{cakeShopId}/cakes")
class CakeController(
    private val cakeService: CakeService
) {

    @GetMapping("/{cakeId}")
    fun details(
        @PathVariable cakeShopId: Long,
        @PathVariable cakeId: Long
    ): ApiResponse<CakeDetailResponse> {
        val result = cakeService.searchCakeDetailById(cakeId)

        return ApiResponse.success(result)
    }

    @PostMapping
    fun create(
        @PathVariable cakeShopId: Long,
        @RequestBody @Valid dto: CakeCreateByAdminRequest
    ): ApiResponse<Unit> {
        cakeService.createCake(dto.toParam(cakeShopId))
        return ApiResponse.success()
    }

    @PutMapping("/{cakeId}")
    fun update(
        @PathVariable cakeShopId: Long,
        @PathVariable cakeId: Long,
        @RequestBody @Valid dto: CakeUpdateByAdminRequest
    ): ApiResponse<Unit> {
        cakeService.updateCake(dto.toParam(cakeShopId))
        return ApiResponse.success()
    }

    @DeleteMapping("/{cakeId}")
    fun delete(
        @PathVariable cakeShopId: Long,
        @PathVariable cakeId: Long
    ): ApiResponse<Unit> {
        cakeService.deleteCake(cakeId)

        return ApiResponse.success()
    }
}

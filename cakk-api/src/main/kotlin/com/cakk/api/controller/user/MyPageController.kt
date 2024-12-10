package com.cakk.api.controller.user

import jakarta.validation.Valid

import org.springframework.web.bind.annotation.*

import com.cakk.api.annotation.SignInUser
import com.cakk.api.dto.request.like.HeartCakeSearchRequest
import com.cakk.api.dto.request.like.HeartCakeShopSearchRequest
import com.cakk.api.dto.request.user.ProfileUpdateRequest
import com.cakk.api.mapper.supplyHeartCakeSearchParamBy
import com.cakk.api.mapper.supplyHeartCakeShopSearchParamBy
import com.cakk.core.dto.response.like.HeartCakeImageListResponse
import com.cakk.core.dto.response.like.HeartCakeShopListResponse
import com.cakk.core.dto.response.user.ProfileInformationResponse
import com.cakk.api.mapper.supplyProfileUpdateParamBy
import com.cakk.core.service.like.HeartService
import com.cakk.core.service.user.UserService
import com.cakk.common.response.ApiResponse
import com.cakk.infrastructure.persistence.entity.user.UserEntity

@RestController
@RequestMapping("/me")
class MyPageController(
    private val userService: UserService,
    private val heartService: HeartService
) {

    @GetMapping
    fun profile(
		@SignInUser userEntity: UserEntity
    ): ApiResponse<ProfileInformationResponse> {
        val response = userService.findProfile(userEntity)

		return ApiResponse.success(response)
    }

    @PutMapping
    fun modify(
		@SignInUser userEntity: UserEntity,
		@RequestBody @Valid request: ProfileUpdateRequest
    ): ApiResponse<Unit> {
		val param = supplyProfileUpdateParamBy(request, userEntity)
        userService.updateInformation(param)

		return ApiResponse.success()
    }

    @DeleteMapping
    fun withdraw(
		@SignInUser userEntity: UserEntity
    ): ApiResponse<Unit> {
        userService.withdraw(userEntity)

		return ApiResponse.success()
    }

    @GetMapping("/heart-cakes")
    fun heartCakeList(
		@SignInUser userEntity: UserEntity,
		@ModelAttribute @Valid request: HeartCakeSearchRequest
    ): ApiResponse<HeartCakeImageListResponse> {
		val param = supplyHeartCakeSearchParamBy(request, userEntity)
        val response = heartService.searchCakeImagesByCursorAndHeart(param)

		return ApiResponse.success(response)
    }

    @GetMapping("/heart-shops")
    fun heartCakeList(
		@SignInUser userEntity: UserEntity,
		@ModelAttribute @Valid request: HeartCakeShopSearchRequest
    ): ApiResponse<HeartCakeShopListResponse> {
		val param = supplyHeartCakeShopSearchParamBy(request, userEntity)
        val response = heartService.searchCakeShopByCursorAndHeart(param)

		return ApiResponse.success(response)
    }
}

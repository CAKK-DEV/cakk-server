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
import com.cakk.domain.mysql.entity.user.User

@RestController
@RequestMapping("/me")
class MyPageController(
    private val userService: UserService,
    private val heartService: HeartService
) {

    @GetMapping
    fun profile(
		@SignInUser user: User
    ): ApiResponse<ProfileInformationResponse> {
        val response = userService.findProfile(user)

		return ApiResponse.success(response)
    }

    @PutMapping
    fun modify(
		@SignInUser user: User,
		@RequestBody @Valid request: ProfileUpdateRequest
    ): ApiResponse<Unit> {
		val param = supplyProfileUpdateParamBy(request, user)
        userService.updateInformation(param)

		return ApiResponse.success()
    }

    @DeleteMapping
    fun withdraw(
		@SignInUser user: User
    ): ApiResponse<Unit> {
        userService.withdraw(user)

		return ApiResponse.success()
    }

    @GetMapping("/heart-cakes")
    fun heartCakeList(
		@SignInUser user: User,
		@ModelAttribute @Valid request: HeartCakeSearchRequest
    ): ApiResponse<HeartCakeImageListResponse> {
		val param = supplyHeartCakeSearchParamBy(request, user)
        val response = heartService.searchCakeImagesByCursorAndHeart(param)

		return ApiResponse.success(response)
    }

    @GetMapping("/heart-shops")
    fun heartCakeList(
		@SignInUser user: User,
		@ModelAttribute @Valid request: HeartCakeShopSearchRequest
    ): ApiResponse<HeartCakeShopListResponse> {
		val param = supplyHeartCakeShopSearchParamBy(request, user)
        val response = heartService.searchCakeShopByCursorAndHeart(param)

		return ApiResponse.success(response)
    }
}

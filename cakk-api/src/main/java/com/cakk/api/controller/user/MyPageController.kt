package com.cakk.api.controller.user

import com.cakk.api.annotation.SignInUser
import com.cakk.api.dto.request.like.HeartCakeSearchRequest
import com.cakk.api.dto.request.like.HeartCakeShopSearchRequest
import com.cakk.api.dto.request.user.ProfileUpdateRequest
import com.cakk.api.dto.response.like.HeartCakeImageListResponse
import com.cakk.api.dto.response.like.HeartCakeShopListResponse
import com.cakk.api.dto.response.user.ProfileInformationResponse
import com.cakk.api.service.like.HeartService
import com.cakk.api.service.user.UserService
import com.cakk.common.response.ApiResponse
import com.cakk.domain.mysql.entity.user.User
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
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
    ): ApiResponse<Void> {
        userService.updateInformation(user, request)

		return ApiResponse.success()
    }

    @DeleteMapping
    fun withdraw(
		@SignInUser user: User
    ): ApiResponse<Void> {
        userService.withdraw(user)

		return ApiResponse.success()
    }

    @GetMapping("/heart-cakes")
    fun heartCakeList(
		@SignInUser user: User,
		@ModelAttribute @Valid request: HeartCakeSearchRequest
    ): ApiResponse<HeartCakeImageListResponse> {
        val response = heartService.searchCakeImagesByCursorAndHeart(request, user)

		return ApiResponse.success(response)
    }

    @GetMapping("/heart-shops")
    fun heartCakeList(
		@SignInUser user: User,
		@ModelAttribute @Valid request: HeartCakeShopSearchRequest
    ): ApiResponse<HeartCakeShopListResponse> {
        val response = heartService.searchCakeShopByCursorAndHeart(request, user)

		return ApiResponse.success(response)
    }
}

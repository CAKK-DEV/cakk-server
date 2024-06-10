package com.cakk.api.controller.user;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.SignInUser;
import com.cakk.api.dto.request.like.LikeCakeSearchRequest;
import com.cakk.api.dto.request.user.ProfileUpdateRequest;
import com.cakk.api.dto.response.like.LikeCakeImageListResponse;
import com.cakk.api.dto.response.user.ProfileInformationResponse;
import com.cakk.api.service.like.LikeService;
import com.cakk.api.service.user.UserService;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.mysql.entity.user.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
public class MyPageController {

	private final UserService userService;
	private final LikeService likeService;

	@GetMapping
	public ApiResponse<ProfileInformationResponse> profile(
		@SignInUser User user
	) {
		final ProfileInformationResponse response = userService.findProfile(user);

		return ApiResponse.success(response);
	}

	@PutMapping
	public ApiResponse<Void> modify(
		@SignInUser User user,
		@RequestBody @Valid ProfileUpdateRequest request
	) {
		userService.updateInformation(user, request);

		return ApiResponse.success();
	}

	@DeleteMapping
	public ApiResponse<Void> withdraw(
		@SignInUser User user
	) {
		userService.withdraw(user);

		return ApiResponse.success();
	}

	@GetMapping("/liked-cakes")
	public ApiResponse<LikeCakeImageListResponse> likedCakeList(
		@SignInUser User user,
		@Valid @ModelAttribute LikeCakeSearchRequest request
	) {
		final LikeCakeImageListResponse response = likeService.findCakeImagesByCursorAndLike(request, user);

		return ApiResponse.success(response);
	}
}

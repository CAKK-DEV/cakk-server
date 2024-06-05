package com.cakk.api.controller.search;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.search.TopSearchedListRequest;
import com.cakk.api.dto.response.search.TopSearchedListResponse;
import com.cakk.api.service.search.KeywordService;
import com.cakk.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class KeywordController {

	private final KeywordService keywordService;

	@GetMapping("/top-searched")
	public ApiResponse<TopSearchedListResponse> topSearched(
		@ModelAttribute @Valid TopSearchedListRequest request
	) {
		final TopSearchedListResponse response = keywordService.findTopSearched(request);

		return ApiResponse.success(response);
	}
}

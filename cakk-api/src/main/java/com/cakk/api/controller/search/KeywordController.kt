package com.cakk.api.controller.search

import com.cakk.api.dto.request.search.TopSearchedListRequest
import com.cakk.api.dto.response.search.TopSearchedListResponse
import com.cakk.api.service.search.KeywordService
import com.cakk.common.response.ApiResponse
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
class KeywordController(
	private val keywordService: KeywordService
) {

    @GetMapping("/top-searched")
    fun topSearched(
            @ModelAttribute @Valid request: TopSearchedListRequest
    ): ApiResponse<TopSearchedListResponse> {
        val response = keywordService.findTopSearched(request)

		return ApiResponse.success(response)
    }
}

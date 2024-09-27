package com.cakk.api.controller.search

import jakarta.validation.Valid

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.cakk.api.dto.request.search.TopSearchedListRequest
import com.cakk.api.mapper.supplyTopSearchedListParamBy
import com.cakk.core.dto.response.search.TopSearchedListResponse
import com.cakk.core.service.search.KeywordService
import com.cakk.common.response.ApiResponse

@RestController
@RequestMapping("/search")
class KeywordController(
	private val keywordService: KeywordService
) {

    @GetMapping("/top-searched")
    fun topSearched(
            @ModelAttribute @Valid request: TopSearchedListRequest
    ): ApiResponse<TopSearchedListResponse> {
		val param = supplyTopSearchedListParamBy(request)
        val response = keywordService.findTopSearched(param)

		return ApiResponse.success(response)
    }
}

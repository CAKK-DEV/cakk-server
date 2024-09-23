package com.cakk.api.controller.s3

import com.cakk.common.response.ApiResponse
import com.cakk.external.service.S3Service
import com.cakk.external.vo.s3.PresignedUrl
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/aws")
class AwsS3Controller(
	private val s3Service: S3Service
) {

	@GetMapping("/img")
	fun getImageUrl(): ApiResponse<PresignedUrl> {
		return ApiResponse.success(s3Service.getPresignedUrlWithImagePath())
	}
}


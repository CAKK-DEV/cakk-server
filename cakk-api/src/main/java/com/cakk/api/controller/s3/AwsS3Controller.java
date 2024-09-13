package com.cakk.api.controller.s3;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.common.response.ApiResponse;
import com.cakk.external.service.S3Service;
import com.cakk.external.vo.s3.PresignedUrl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/aws")
public class AwsS3Controller {

	private final S3Service s3Service;

	@GetMapping("/img")
	public ApiResponse<PresignedUrl> getImageUrl() {
		return ApiResponse.success(s3Service.getPresignedUrlWithImagePath());
	}

}

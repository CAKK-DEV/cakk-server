package com.cakk.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockMvcTest;
import com.cakk.external.vo.PresignedUrl;

public class AwsS3ControllerTest extends MockMvcTest {

	@TestWithDisplayName("presigned url을 반환한다.")
	void getImageUrl() throws Exception {
		// given
		final PresignedUrl presignedUrl = getConstructorMonkey().giveMeBuilder(PresignedUrl.class)
			.set("imagePath", Arbitraries.strings().alpha().numeric().ofMinLength(1).ofMaxLength(100))
			.set("imageUrl", Arbitraries.strings().alpha().numeric().ofMinLength(1).ofMaxLength(100))
			.set("presignedUrl", Arbitraries.strings().alpha().numeric().ofMinLength(1).ofMaxLength(100))
			.sample();

		doReturn(presignedUrl).when(s3Service).getPresignedUrlWithImagePath();

		// when & then
		mockMvc.perform(get("/aws/img"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.imagePath").value(presignedUrl.imagePath()))
			.andExpect(jsonPath("$.data.imageUrl").value(presignedUrl.imageUrl()))
			.andExpect(jsonPath("$.data.presignedUrl").value(presignedUrl.presignedUrl()));
	}
}

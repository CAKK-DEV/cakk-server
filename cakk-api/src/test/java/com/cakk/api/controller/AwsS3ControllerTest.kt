package com.cakk.api.controller

import org.mockito.Mockito.doReturn
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.MockMvcTest
import com.cakk.api.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.api.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.external.vo.s3.PresignedUrl

internal class AwsS3ControllerTest : MockMvcTest() {

    @TestWithDisplayName("presigned url을 반환한다.")
    fun getImageUrl() {
            // given
            val presignedUrl = fixtureMonkey.giveMeBuilder(PresignedUrl::class.java)
                .set("imagePath", getStringFixtureBw(1, 200))
                .set("imageUrl", getStringFixtureBw(1, 200))
                .set("presignedUrl", getStringFixtureBw(1, 200))
                .sample()

            doReturn(presignedUrl).`when`(s3Service).getPresignedUrlWithImagePath()

            // when & then
            mockMvc.perform(get("/aws/img"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.imagePath").value(presignedUrl.imagePath))
                .andExpect(jsonPath("$.data.imageUrl").value(presignedUrl.imageUrl))
                .andExpect(jsonPath("$.data.presignedUrl").value(presignedUrl.presignedUrl))
        }
}

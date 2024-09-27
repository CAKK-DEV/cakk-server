package com.cakk.api.common.base

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

import com.fasterxml.jackson.databind.ObjectMapper

import com.cakk.api.controller.advice.GlobalControllerAdvice
import com.cakk.api.controller.s3.AwsS3Controller
import com.cakk.api.controller.shop.ShopController
import com.cakk.api.controller.user.SignController
import com.cakk.api.filter.JwtAuthenticationFilter
import com.cakk.core.listener.ErrorAlertEventListener
import com.cakk.core.service.like.HeartService
import com.cakk.core.service.like.LikeService
import com.cakk.core.service.shop.ShopService
import com.cakk.core.service.user.EmailVerificationService
import com.cakk.core.service.user.SignService
import com.cakk.core.service.views.ViewsService
import com.cakk.external.service.S3Service

@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebMvcTest(
    properties = ["spring.profiles.active=test"],
    value = [
        SignController::class,
        ShopController::class,
        AwsS3Controller::class,
        GlobalControllerAdvice::class
    ]
)
abstract class MockMvcTest {

	@Autowired
    protected lateinit var mockMvc: MockMvc


	@Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockBean
    protected lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

	@MockBean
    protected lateinit var s3Service: S3Service

	@MockBean
    protected lateinit var signService: SignService

    @MockBean
    protected lateinit var emailVerificationService: EmailVerificationService

	@MockBean
    protected lateinit var shopService: ShopService

    @MockBean
    protected lateinit var heartService: HeartService

    @MockBean
    protected lateinit var likeService: LikeService

    @MockBean
    protected lateinit var viewsService: ViewsService

	@MockBean
    protected lateinit var errorAlertEventListener: ErrorAlertEventListener

    @BeforeEach
    fun setup(webApplicationContext: WebApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .build()
    }
}

package com.cakk.api.controller.user

import com.cakk.api.annotation.AccessToken
import com.cakk.api.annotation.RefreshToken
import com.cakk.api.dto.request.user.GenerateCodeRequest
import com.cakk.api.dto.request.user.UserSignInRequest
import com.cakk.api.dto.request.user.UserSignUpRequest
import com.cakk.api.dto.request.user.VerifyEmailRequest
import com.cakk.api.dto.response.user.JwtResponse
import com.cakk.api.mapper.UserMapper
import com.cakk.api.service.user.SignService
import com.cakk.common.response.ApiResponse
import com.cakk.core.service.user.EmailVerificationService
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class SignController(
	private val signService: SignService,
	private val emailVerificationService: EmailVerificationService
) {

    @PostMapping("/sign-up")
    fun signUp(
		@RequestBody @Valid request: UserSignUpRequest
    ): ApiResponse<JwtResponse> {
		val param = UserMapper.supplyUserSignUpParamBy(request)
        return ApiResponse.success(signService.signUp(param))
    }

    @PostMapping("/sign-in")
    fun signIn(
		@RequestBody @Valid request: UserSignInRequest
    ): ApiResponse<JwtResponse> {
		val param = UserMapper.supplyUserSignInParamBy(request)
		return ApiResponse.success(signService.signIn(param))
	}

    @PostMapping("/sign-out")
    fun signOut(
		@AccessToken accessToken: String?,
		@RefreshToken refreshToken: String?
    ): ApiResponse<Void> {
        signService.signOut(accessToken, refreshToken)

		return ApiResponse.success()
    }

    @PostMapping("/recreate-token")
    fun recreate(
		@RefreshToken refreshToken: String?
    ): ApiResponse<JwtResponse> {
        val response = signService.recreateToken(refreshToken)

		return ApiResponse.success(response)
    }

    @PostMapping("/email/request-code")
    fun sendEmailForVerification(
		@RequestBody @Valid request: GenerateCodeRequest
    ): ApiResponse<Void> {
        val param = UserMapper.supplyGenerateCodeParamBy(request)
        emailVerificationService.sendEmailForVerification(param)

		return ApiResponse.success()
    }

    @PostMapping("/email/verify-email")
    fun verifyEmail(
            @RequestBody @Valid request: VerifyEmailRequest
    ): ApiResponse<Void> {
        val param = UserMapper.supplyVerifyEmailParamBy(request)
        emailVerificationService.checkEmailVerificationCode(param)

		return ApiResponse.success()
    }
}

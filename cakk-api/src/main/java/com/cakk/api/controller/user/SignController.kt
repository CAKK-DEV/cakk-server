package com.cakk.api.controller.user

import jakarta.validation.Valid

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import com.cakk.api.annotation.AccessToken
import com.cakk.api.annotation.RefreshToken
import com.cakk.api.dto.request.user.GenerateCodeRequest
import com.cakk.api.dto.request.user.UserSignInRequest
import com.cakk.api.dto.request.user.UserSignUpRequest
import com.cakk.api.dto.request.user.VerifyEmailRequest
import com.cakk.core.dto.response.user.JwtResponse
import com.cakk.api.mapper.supplyGenerateCodeParamBy
import com.cakk.api.mapper.supplyUserSignInParamBy
import com.cakk.api.mapper.supplyUserSignUpParamBy
import com.cakk.api.mapper.supplyVerifyEmailParamBy
import com.cakk.api.service.user.SignService
import com.cakk.common.response.ApiResponse
import com.cakk.core.service.user.EmailVerificationService

@RestController
class SignController(
	private val signService: SignService,
	private val emailVerificationService: EmailVerificationService
) {

    @PostMapping("/sign-up")
    fun signUp(
		@RequestBody @Valid request: UserSignUpRequest
    ): ApiResponse<JwtResponse> {
		val param = supplyUserSignUpParamBy(request)
        return ApiResponse.success(signService.signUp(param))
    }

    @PostMapping("/sign-in")
    fun signIn(
		@RequestBody @Valid request: UserSignInRequest
    ): ApiResponse<JwtResponse> {
		val param = supplyUserSignInParamBy(request)
		return ApiResponse.success(signService.signIn(param))
	}

    @PostMapping("/sign-out")
    fun signOut(
		@AccessToken accessToken: String?,
		@RefreshToken refreshToken: String?
    ): ApiResponse<Unit> {
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
    ): ApiResponse<Unit> {
        val param = supplyGenerateCodeParamBy(request)
        emailVerificationService.sendEmailForVerification(param)

		return ApiResponse.success()
    }

    @PostMapping("/email/verify-email")
    fun verifyEmail(
            @RequestBody @Valid request: VerifyEmailRequest
    ): ApiResponse<Unit> {
        val param = supplyVerifyEmailParamBy(request)
        emailVerificationService.checkEmailVerificationCode(param)

		return ApiResponse.success()
    }
}
